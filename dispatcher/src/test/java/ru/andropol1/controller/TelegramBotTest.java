package ru.andropol1.controller;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.andropol1.config.TelegramBotProperties;

import static org.mockito.Mockito.*;

class TelegramBotTest {
	@Test
	void sendAnswerMessageSuccess() throws TelegramApiException {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId("1");
		sendMessage.setText("Test");
		TelegramBotProperties botProperties = mock(TelegramBotProperties.class);
		when(botProperties.getUri()).thenReturn("test");
		when(botProperties.getName()).thenReturn("test");
		when(botProperties.getToken()).thenReturn("test");
		TelegramBot telegramBot = new TelegramBot(botProperties);
		TelegramBot spyTelegramBot = spy(telegramBot);
		spyTelegramBot.sendAnswerMessage(sendMessage);
		verify(spyTelegramBot).execute(sendMessage);

	}

}