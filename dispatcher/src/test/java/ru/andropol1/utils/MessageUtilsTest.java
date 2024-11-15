package ru.andropol1.utils;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageUtilsTest {
	@Test
	void generateResponseMessageSuccess(){
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		Long chatId = 1L;
		String text = "Test";
		when(update.getMessage()).thenReturn(message);
		when(message.getChatId()).thenReturn(chatId);
		MessageUtils messageUtils = new MessageUtils();
		SendMessage sendMessage = messageUtils.generateResponseMessage(update, text);
		assertNotNull(sendMessage);
		assertEquals(chatId.toString(), sendMessage.getChatId());
		assertEquals(text, sendMessage.getText());
	}

}