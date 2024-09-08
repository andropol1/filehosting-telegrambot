package ru.andropol1.service;

import ru.andropol1.dto.MailParams;

public interface KafkaConsumer {
	void consume(MailParams mailParams);
}
