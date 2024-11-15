package ru.andropol1.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUtils {
	public SendMessage generateResponseMessage(Update update, String text) {
		Message message = update.getMessage();
		SendMessage response = new SendMessage();
		response.setChatId(message.getChatId());
		response.setText(text);
		return response;
	}
}
