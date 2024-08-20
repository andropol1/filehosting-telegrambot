package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface KafkaConsumer {
	void consume(SendMessage sendMessage);
}
