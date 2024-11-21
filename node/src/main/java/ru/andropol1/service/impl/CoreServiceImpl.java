package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.entity.AppUser;
import ru.andropol1.entity.TelegramMessage;
import ru.andropol1.enums.UserState;
import ru.andropol1.repository.AppUserRepository;
import ru.andropol1.repository.TelegramMessageRepository;
import ru.andropol1.service.CoreService;

import static ru.andropol1.enums.ServiceCommands.*;
import static ru.andropol1.enums.UserState.BASIC_STATE;

@Service
@Log4j
public class CoreServiceImpl implements CoreService {
	private final KafkaProducerImpl kafkaProducer;
	private final AppUserServiceImpl appUserService;
	private final AppUserRepository appUserRepository;
	private final TelegramMessageRepository telegramMessageRepository;

	@Autowired
	public CoreServiceImpl(KafkaProducerImpl kafkaProducer, AppUserServiceImpl appUserService, AppUserRepository appUserRepository, TelegramMessageRepository telegramMessageRepository) {
		this.kafkaProducer = kafkaProducer;
		this.appUserService = appUserService;
		this.appUserRepository = appUserRepository;
		this.telegramMessageRepository = telegramMessageRepository;
	}

	@Override
	public void sendAnswer(String output, Long chatId) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText(output);
		sendMessage.setChatId(chatId);
		kafkaProducer.produceAnswerMessage(sendMessage);
	}

	@Override
	public String processServiceCommand(AppUser appUser, String cmd) {
		if (REGISTRATION.equals(cmd)){
			return appUserService.registerUser(appUser);
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

	@Override
	public String cancelProcess(AppUser appUser) {
		appUser.setUserState(BASIC_STATE);
		appUserRepository.save(appUser);
		return "Команда отменена!";
	}

	@Override
	public void saveUpdate(Update update) {
		TelegramMessage telegramMessage = TelegramMessage.builder()
														 .update(update)
														 .build();
		telegramMessageRepository.save(telegramMessage);
	}

	boolean isNotAllowedToSendContent(Long chatId, AppUser appUser) {
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
}
