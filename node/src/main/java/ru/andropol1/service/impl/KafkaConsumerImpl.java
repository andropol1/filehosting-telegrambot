package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.entity.TelegramMessage;
import ru.andropol1.repository.TelegramMessageRepository;
import ru.andropol1.service.KafkaConsumer;
import ru.andropol1.service.KafkaProducer;

@Service
@Log4j
public class KafkaConsumerImpl implements KafkaConsumer {
	private final KafkaProperties kafkaProperties;
	private final KafkaProducer kafkaProducer;
	private final  TelegramMessageRepository telegramMessageRepository;

	@Autowired
	public KafkaConsumerImpl(KafkaProperties kafkaProperties, KafkaProducer kafkaProducer,
							 TelegramMessageRepository telegramMessageRepository) {
		this.kafkaProperties = kafkaProperties;
		this.kafkaProducer = kafkaProducer;
		this.telegramMessageRepository = telegramMessageRepository;
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getText_message()}", groupId = "group")
	public void consumeTextMessage(Update update) {
		log.debug("consumeTextMessage");
		saveUpdate(update);
		Message message = update.getMessage();
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
}
