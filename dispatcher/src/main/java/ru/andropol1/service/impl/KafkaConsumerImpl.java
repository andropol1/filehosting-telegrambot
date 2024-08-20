package ru.andropol1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.controller.TelegramBot;
import ru.andropol1.service.KafkaConsumer;

@Service
public class KafkaConsumerImpl implements KafkaConsumer {
	private final KafkaProperties kafkaProperties;
	private final TelegramBot telegramBot;

	@Autowired
	public KafkaConsumerImpl(KafkaProperties kafkaProperties, TelegramBot telegramBot) {
		this.kafkaProperties = kafkaProperties;
		this.telegramBot = telegramBot;
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getAnswer_message()}", groupId = "group")
	public void consume(SendMessage sendMessage) {
		telegramBot.sendAnswerMessage(sendMessage);
	}
}
