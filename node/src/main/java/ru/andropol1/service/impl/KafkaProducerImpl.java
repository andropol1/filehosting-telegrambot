package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.dto.MailParams;
import ru.andropol1.service.KafkaProducer;

@Service
@Log4j
public class KafkaProducerImpl implements KafkaProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final KafkaProperties kafkaProperties;
	@Autowired
	public KafkaProducerImpl(KafkaTemplate<String, Object> kafkaTemplate, KafkaProperties kafkaProperties) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaProperties = kafkaProperties;
	}

	@Override
	public void produceAnswerMessage(SendMessage sendMessage) {
		log.debug(sendMessage.getText());
		kafkaTemplate.send(kafkaProperties.getAnswer_message(), sendMessage);
	}

	@Override
	public void produceRegistrationMessage(MailParams mailParams) {
		log.debug(mailParams.getEmailTo());
		kafkaTemplate.send(kafkaProperties.getRegistration_message(), mailParams);
	}
}
