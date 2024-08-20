package ru.andropol1.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.andropol1.config.TelegramBotProperties;
import ru.andropol1.utils.MessageUtils;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {

	private TelegramBotProperties telegramBotProperties;
	private final MessageUtils messageUtils;

	@Autowired
	public TelegramBot(TelegramBotProperties telegramBotProperties, MessageUtils messageUtils) {
		this.telegramBotProperties = telegramBotProperties;
		this.messageUtils = messageUtils;
	}

	@Override
	public String getBotUsername() {
		return telegramBotProperties.getName();
	}

	@Override
	public String getBotToken() {
		return telegramBotProperties.getToken();
	}

	@Override
	public void onUpdateReceived(Update update) {
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
	private void sendAnswerMessage(SendMessage sendMessage) {
		if (sendMessage != null) {
			try {
				execute(sendMessage);
			} catch (TelegramApiException e) {
				log.error(e.getMessage());
			}
		}
	}
	private void filterMessagesByContent(Update update) {
		Message message = update.getMessage();
		if (message.getText() != null){
			messageUtils.processTextMessage(update);
			setFileIsReceived(update);
		} else if (message.getDocument() != null) {
			messageUtils.processDocMessage(update);
			setFileIsReceived(update);
		} else if (message.getPhoto() != null) {
			messageUtils.processPhotoMessage(update);
			setFileIsReceived(update);
		} else {
			setUnsupportedMessage(update);
		}
	}
	private void setUnsupportedMessage(Update update) {
		SendMessage responseMessage = messageUtils.generateResponseMessage(update, "Неподдерживаемый тип сообщения");
		sendAnswerMessage(responseMessage);
	}
	private void setFileIsReceived(Update update){
		SendMessage responseMessage = messageUtils.generateResponseMessage(update, "Файл обрабатывается...");
		sendAnswerMessage(responseMessage);
	}
}
