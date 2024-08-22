package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.entity.AppUser;
import ru.andropol1.entity.TelegramMessage;
import ru.andropol1.repository.AppUserRepository;
import ru.andropol1.repository.TelegramMessageRepository;
import ru.andropol1.service.KafkaConsumer;
import ru.andropol1.service.KafkaProducer;

import static ru.andropol1.enums.UserState.BASIC_STATE;

@Service
@Log4j
public class KafkaConsumerImpl implements KafkaConsumer {
	private final KafkaProperties kafkaProperties;
	private final KafkaProducer kafkaProducer;
	private final  TelegramMessageRepository telegramMessageRepository;
	private final AppUserRepository appUserRepository;

	@Autowired
	public KafkaConsumerImpl(KafkaProperties kafkaProperties, KafkaProducer kafkaProducer,
							 TelegramMessageRepository telegramMessageRepository, AppUserRepository appUserRepository) {
		this.kafkaProperties = kafkaProperties;
		this.kafkaProducer = kafkaProducer;
		this.telegramMessageRepository = telegramMessageRepository;
		this.appUserRepository = appUserRepository;
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getText_message()}", groupId = "group")
	public void consumeTextMessage(Update update) {
		log.debug("consumeTextMessage");
		saveUpdate(update);
		Message message = update.getMessage();
		User user = message.getFrom();
		AppUser appUser = findOrSaveAppUser(user);
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText("Node");
		sendMessage.setChatId(message.getChatId());
		kafkaProducer.produce(sendMessage);
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getDoc_message()}", groupId = "group")
	public void consumeDocMessage(Update update) {
		log.debug("consumeDocMessage");
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getPhoto_message()}", groupId = "group")
	public void consumePhotoMessage(Update update) {
		log.debug("consumePhotoMessage");
	}
	private void saveUpdate(Update update){
		TelegramMessage telegramMessage = TelegramMessage.builder()
				.update(update)
				.build();
		telegramMessageRepository.save(telegramMessage);
	}
	private AppUser findOrSaveAppUser(User user){
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
