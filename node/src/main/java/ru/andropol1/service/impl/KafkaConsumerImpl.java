package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.entity.AppUser;
import ru.andropol1.entity.TelegramMessage;
import ru.andropol1.enums.LinkType;
import ru.andropol1.enums.UserState;
import ru.andropol1.exceptions.UploadFileException;
import ru.andropol1.repository.AppUserRepository;
import ru.andropol1.repository.TelegramMessageRepository;
import ru.andropol1.service.FileService;
import ru.andropol1.service.KafkaConsumer;
import ru.andropol1.service.KafkaProducer;

import static ru.andropol1.enums.ServiceCommands.*;
import static ru.andropol1.enums.UserState.BASIC_STATE;
import static ru.andropol1.enums.UserState.WAIT_FOR_EMAIL_STATE;

@Service
@Log4j
public class KafkaConsumerImpl implements KafkaConsumer {
	private final KafkaProperties kafkaProperties;
	private final KafkaProducer kafkaProducer;
	private final  TelegramMessageRepository telegramMessageRepository;
	private final AppUserRepository appUserRepository;
	private final FileService fileService;

	@Autowired
	public KafkaConsumerImpl(KafkaProperties kafkaProperties, KafkaProducer kafkaProducer,
							 TelegramMessageRepository telegramMessageRepository, AppUserRepository appUserRepository, FileService fileService) {
		this.kafkaProperties = kafkaProperties;
		this.kafkaProducer = kafkaProducer;
		this.telegramMessageRepository = telegramMessageRepository;
		this.appUserRepository = appUserRepository;
		this.fileService = fileService;
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getText_message()}", groupId = "group")
	public void consumeTextMessage(Update update) {
		log.debug("consumeTextMessage");
		saveUpdate(update);
		AppUser appUser = findOrSaveAppUser(update);
		UserState userState = appUser.getUserState();
		String cmd = update.getMessage().getText();
		String output = "";
		if (CANCEL.equals(cmd)){
			output = cancelProcess(appUser);
		} else if (BASIC_STATE.equals(userState)) {
			output = processServiceCommand(appUser, cmd);
		} else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
			//TODO
		} else {
			log.error("Unknown user state: " + userState);
			output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
		}
		Long chatId = update.getMessage().getChatId();
		sendAnswer(output, chatId);
	}

	private void sendAnswer(String output, Long chatId) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText(output);
		sendMessage.setChatId(chatId);
		kafkaProducer.produce(sendMessage);
	}

	private String processServiceCommand(AppUser appUser, String cmd) {
		if (REGISTRATION.equals(cmd)){
			//TODO
			return "Временно недоступно.";
		} else if (HELP.equals(cmd)) {
			return "Cписок доступных команд:\n"
					+ "/cancel - отмена выполнения текущей команды;\n"
					+ "/registration - регистрация пользователя.";
		} else if (START.equals(cmd)) {
			return "Привет! Чтобы посмотреть список доступных команд введите /help";
		} else {
			return "Неверная команда! Чтобы посмотреть список доступных команд введите /help";
		 }
	}

	private String cancelProcess(AppUser appUser) {
		appUser.setUserState(BASIC_STATE);
		appUserRepository.save(appUser);
		return "Команда отменена!";
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getDoc_message()}", groupId = "group")
	public void consumeDocMessage(Update update) {
		log.debug("consumeDocMessage");
		saveUpdate(update);
		AppUser appUser = findOrSaveAppUser(update);
		Long chatId = update.getMessage().getChatId();
		if (isNotAllowedToSendContent(chatId, appUser)){
			return;
		}
		try{
			AppDocument appDocument = fileService.processDoc(update.getMessage());
			String link = fileService.generateLink(appDocument.getId(), LinkType.GET_DOC);
			String answer = "Документ успешно загружен! Ссылка для скачивания: " + link;
			sendAnswer(answer, chatId);
		} catch (UploadFileException e){
			log.error(e);
			String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
			sendAnswer(error, chatId);
		}
	}
	@Override
	@KafkaListener(topics = "#{kafkaProperties.getPhoto_message()}", groupId = "group")
	public void consumePhotoMessage(Update update) {
		log.debug("consumePhotoMessage");
		saveUpdate(update);
		AppUser appUser = findOrSaveAppUser(update);
		Long chatId = update.getMessage().getChatId();
		if (isNotAllowedToSendContent(chatId, appUser)){
			return;
		}
		try	{
			AppPhoto appPhoto = fileService.processPhoto(update.getMessage());
			String link = fileService.generateLink(appPhoto.getId(), LinkType.GET_PHOTO);
			String answer = "Фото успешно загружено! Ссылка для скачивания: " + link;
			sendAnswer(answer, chatId);
		} catch (UploadFileException e){
			log.error(e);
			String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
			sendAnswer(error, chatId);
		}
	}
	private boolean isNotAllowedToSendContent(Long chatId, AppUser appUser) {
		UserState userState = appUser.getUserState();
		if (!appUser.getIsActive()){
			String error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента.";
			sendAnswer(error, chatId);
			return true;
		} else if (!BASIC_STATE.equals(userState)){
			String error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
			sendAnswer(error, chatId);
			return true;
		}
		return false;
	}
	private void saveUpdate(Update update){
		TelegramMessage telegramMessage = TelegramMessage.builder()
				.update(update)
				.build();
		telegramMessageRepository.save(telegramMessage);
	}
	private AppUser findOrSaveAppUser(Update update){
		User user = update.getMessage().getFrom();
		AppUser persistentUser = appUserRepository.findAppUserByTelegramUserId(user.getId());
		if (persistentUser == null){
			AppUser transientUser = AppUser.builder()
					.telegramUserId(user.getId())
					.userName(user.getUserName())
					.firstName(user.getFirstName())
					.lastName(user.getLastName())
					.isActive(true)
					.userState(BASIC_STATE)
					.build();
			return appUserRepository.save(transientUser);
		}
		return persistentUser;
	}
}
