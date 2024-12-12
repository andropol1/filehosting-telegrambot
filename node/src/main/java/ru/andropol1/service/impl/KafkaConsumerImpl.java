package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.entity.AppUser;
import ru.andropol1.enums.LinkType;
import ru.andropol1.enums.UserState;
import ru.andropol1.exceptions.UploadFileException;
import ru.andropol1.service.AppUserService;
import ru.andropol1.service.FileService;
import ru.andropol1.service.KafkaConsumer;

import static ru.andropol1.enums.ServiceCommands.CANCEL;
import static ru.andropol1.enums.UserState.BASIC_STATE;
import static ru.andropol1.enums.UserState.WAIT_FOR_EMAIL_STATE;

@Service
@Log4j
public class KafkaConsumerImpl implements KafkaConsumer {
	private final KafkaProperties kafkaProperties;
	private final FileService fileService;
	private final AppUserService appUserService;
	private final CoreServiceImpl coreService;

	@Autowired
	public KafkaConsumerImpl(KafkaProperties kafkaProperties, FileService fileService,
							 AppUserService appUserService, CoreServiceImpl coreService) {
		this.kafkaProperties = kafkaProperties;
		this.fileService = fileService;
		this.appUserService = appUserService;
		this.coreService = coreService;
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getText_message()}", groupId = "telegram")
	@Transactional
	public void consumeTextMessage(Update update) {
		log.debug("consumeTextMessage");
		coreService.saveUpdate(update);
		AppUser appUser =  appUserService.findOrSaveAppUser(update);
		UserState userState = appUser.getUserState();
		String text = update.getMessage().getText();
		String output = "";
		if (CANCEL.equals(text)){
			output = coreService.cancelProcess(appUser);
		} else if (BASIC_STATE.equals(userState)) {
			output = coreService.processServiceCommand(appUser, text);
		} else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
			output = appUserService.setEmail(appUser, text);
		} else {
			log.error("Unknown user state: " + userState);
			output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
		}
		Long chatId = update.getMessage().getChatId();
		coreService.sendAnswer(output, chatId);
	}
	@Override
	@KafkaListener(topics = "#{kafkaProperties.getDoc_message()}", groupId = "telegram")
	@Transactional
	public void consumeDocMessage(Update update) {
		log.debug("consumeDocMessage");
		coreService.saveUpdate(update);
		AppUser appUser =  appUserService.findOrSaveAppUser(update);
		Long chatId = update.getMessage().getChatId();
		if (coreService.isNotAllowedToSendContent(chatId, appUser)){
			return;
		}
		try{
			AppDocument appDocument = fileService.processDoc(update.getMessage());
			String link = fileService.generateLink(appDocument.getId(), LinkType.GET_DOC);
			String answer = "Документ успешно загружен! Ссылка для скачивания: " + link;
			coreService.sendAnswer(answer, chatId);
		} catch (UploadFileException e){
			log.error(e);
			String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
			coreService.sendAnswer(error, chatId);
		}
	}
	@Override
	@KafkaListener(topics = "#{kafkaProperties.getPhoto_message()}", groupId = "telegram")
	@Transactional
	public void consumePhotoMessage(Update update) {
		log.debug("consumePhotoMessage");
		coreService.saveUpdate(update);
		AppUser appUser = appUserService.findOrSaveAppUser(update);
		Long chatId = update.getMessage().getChatId();
		if (coreService.isNotAllowedToSendContent(chatId, appUser)){
			return;
		}
		try	{
			AppPhoto appPhoto = fileService.processPhoto(update.getMessage());
			String link = fileService.generateLink(appPhoto.getId(), LinkType.GET_PHOTO);
			String answer = "Фото успешно загружено! Ссылка для скачивания: " + link;
			coreService.sendAnswer(answer, chatId);
		} catch (UploadFileException e){
			log.error(e);
			String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
			coreService.sendAnswer(error, chatId);
		}
	}
}
