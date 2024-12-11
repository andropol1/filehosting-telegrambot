package ru.andropol1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.andropol1.configuration.KafkaProperties;
import ru.andropol1.dto.MailParams;
import ru.andropol1.service.KafkaConsumer;
import ru.andropol1.service.MailSenderService;

@Service
public class KafkaConsumerImpl implements KafkaConsumer {
	private final KafkaProperties kafkaProperties;
	private final MailSenderService mailSenderService;

	@Autowired
	public KafkaConsumerImpl(KafkaProperties kafkaProperties, MailSenderService mailSenderService) {
		this.kafkaProperties = kafkaProperties;
		this.mailSenderService = mailSenderService;
	}

	@Override
	@KafkaListener(topics = "#{kafkaProperties.getRegistration_message()}", groupId = "telegram")
	public void consume(MailParams mailParams) {
		mailSenderService.send(mailParams);
	}
}
