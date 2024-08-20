package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface KafkaProducer {
	void produce(SendMessage sendMessage);
}
