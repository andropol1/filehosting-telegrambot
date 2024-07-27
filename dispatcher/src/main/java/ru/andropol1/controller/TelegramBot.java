package ru.andropol1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.andropol1.config.TelegramBotProperties;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
	@Autowired
	private TelegramBotProperties telegramBotProperties;

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
		Message message = update.getMessage();
		log.info(message.getText());

		SendMessage response = new SendMessage();
		response.setChatId(message.getChatId());
		response.setText("Hello from bot");
		sendAnswerMessage(response);
	}
	public void sendAnswerMessage(SendMessage sendMessage){
		if (sendMessage != null){
			try{
				execute(sendMessage);
			} catch (TelegramApiException e){
				log.error(e.getMessage());
			}
		}
	}
}
