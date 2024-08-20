package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.andropol1.service.KafkaProducer;

@Service
@Log4j
public class KafkaProducerImpl implements KafkaProducer {
	private final KafkaTemplate kafkaTemplate;
	public KafkaProducerImpl(KafkaTemplate kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public void produce(SendMessage sendMessage) {
		log.debug(sendMessage.getText());
		kafkaTemplate.send("answer_message", sendMessage);
	}
}
