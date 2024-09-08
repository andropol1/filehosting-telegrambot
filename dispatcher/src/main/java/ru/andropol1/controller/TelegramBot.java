package ru.andropol1.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.andropol1.config.TelegramBotProperties;

import javax.annotation.PostConstruct;

@Component
@Log4j
public class TelegramBot extends TelegramWebhookBot {

	private TelegramBotProperties telegramBotProperties;

	@Autowired
	public TelegramBot(TelegramBotProperties telegramBotProperties) {
		this.telegramBotProperties = telegramBotProperties;
		try {
			SetWebhook setWebhook = SetWebhook.builder()
											  .url(telegramBotProperties.getUri())
											  .build();
			this.setWebhook(setWebhook);
		} catch (TelegramApiException e) {
			log.error(e);
		}
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
	public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
		return null;
	}

	@Override
	public String getBotPath() {
		return "/update";
	}

	public void sendAnswerMessage(SendMessage sendMessage) {
		if (sendMessage != null) {
			try {
				execute(sendMessage);
			} catch (TelegramApiException e) {
				log.error(e.getMessage());
			}
		}
	}
}
