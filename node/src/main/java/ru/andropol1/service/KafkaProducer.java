package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.andropol1.dto.MailParams;

public interface KafkaProducer {
	void produceAnswerMessage(SendMessage sendMessage);
	void produceRegistrationMessage(MailParams mailParams);

}
