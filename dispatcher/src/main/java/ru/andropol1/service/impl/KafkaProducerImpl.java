package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.service.KafkaProducer;
@Service
@Log4j
public class KafkaProducerImpl implements KafkaProducer {
	@Override
	public void produce(String topic, Update update) {
		log.debug(update.getMessage().getText());
	}
}
