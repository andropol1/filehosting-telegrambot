package ru.andropol1.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.service.KafkaProducer;

@Component
public class MessageUtils {
	private final KafkaProducer kafkaProducer;
	private final KafkaProperties kafkaProperties;
	@Autowired
	public MessageUtils(KafkaProducer kafkaProducer, KafkaProperties kafkaProperties) {
		this.kafkaProducer = kafkaProducer;
		this.kafkaProperties = kafkaProperties;
	}

	public SendMessage generateResponseMessage(Update update, String text){
		Message message = update.getMessage();
		SendMessage response = new SendMessage();
		response.setChatId(message.getChatId());
		response.setText(text);
		return response;
	}

	public void processPhotoMessage(Update update) {
		kafkaProducer.produce(kafkaProperties.getPhoto_message(), update);
	}

	public void processDocMessage(Update update) {
		kafkaProducer.produce(kafkaProperties.getDoc_message(), update);
	}

	public void processTextMessage(Update update) {
		kafkaProducer.produce(kafkaProperties.getText_message(), update);
	}

}
