package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface KafkaProducer {
	void produce(String topic, Update update);
}
