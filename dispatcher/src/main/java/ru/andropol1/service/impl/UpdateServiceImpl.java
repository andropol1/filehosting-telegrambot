package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.controller.TelegramBot;
import ru.andropol1.service.KafkaProducer;
import ru.andropol1.service.UpdateService;
import ru.andropol1.utils.MessageUtils;

@Service
@Log4j
public class UpdateServiceImpl implements UpdateService {
	private final TelegramBot telegramBot;
	private final KafkaProducer kafkaProducer;
	private final KafkaProperties kafkaProperties;
	private final MessageUtils messageUtils;
	@Autowired

	public UpdateServiceImpl(TelegramBot telegramBot, KafkaProducer kafkaProducer, KafkaProperties kafkaProperties,
							 MessageUtils messageUtils) {
		this.telegramBot = telegramBot;
		this.kafkaProducer = kafkaProducer;
		this.kafkaProperties = kafkaProperties;
		this.messageUtils = messageUtils;
	}

	@Override
	public void processUpdate(Update update) {
		if (update == null){
			log.error("Received update is null");
			return;
		}
		if (update.getMessage() != null){
			filterMessagesByContent(update);
		} else {
			log.error("Unsupported message type is received: " + update);
		}
	}
	private void filterMessagesByContent(Update update) {
		Message message = update.getMessage();
		if (message.hasText()){
			kafkaProducer.produce(kafkaProperties.getText_message(), update);
		} else if (message.hasDocument()) {
			setFileIsReceived(update);
			kafkaProducer.produce(kafkaProperties.getDoc_message(), update);
		} else if (message.hasPhoto()) {
			setFileIsReceived(update);
			kafkaProducer.produce(kafkaProperties.getPhoto_message(), update);
		} else {
			setUnsupportedMessage(update);
		}
	}

	private void setUnsupportedMessage(Update update) {
		SendMessage responseMessage = messageUtils.generateResponseMessage(update, "Неподдерживаемый тип сообщения");
		telegramBot.sendAnswerMessage(responseMessage);
	}
	private void setFileIsReceived(Update update){
		SendMessage responseMessage = messageUtils.generateResponseMessage(update, "Файл обрабатывается...");
		telegramBot.sendAnswerMessage(responseMessage);
	}
}
