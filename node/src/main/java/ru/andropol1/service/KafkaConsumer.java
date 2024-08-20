package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface KafkaConsumer {
	void consumeTextMessage(Update update);
	void consumeDocMessage(Update update);
	void consumePhotoMessage(Update update);
}
