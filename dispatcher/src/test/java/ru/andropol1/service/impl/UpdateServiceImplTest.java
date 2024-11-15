package ru.andropol1.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.config.KafkaProperties;
import ru.andropol1.controller.TelegramBot;
import ru.andropol1.service.KafkaProducer;
import ru.andropol1.utils.MessageUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateServiceImplTest {
	@Mock
	TelegramBot telegramBot;
	@Mock
	KafkaProducer kafkaProducer;
	@Mock
	KafkaProperties kafkaProperties;
	@Mock
	MessageUtils messageUtils;
	@InjectMocks
	UpdateServiceImpl updateService;
	@Test
	void processUpdate_NullUpdate(){
		updateService.processUpdate(null);
		verifyNoInteractions(telegramBot, kafkaProducer, kafkaProperties, messageUtils);
	}
	@Test
	void processUpdate_NullMessage(){
		Update update = mock(Update.class);
		when(update.getMessage()).thenReturn(null);
		updateService.processUpdate(update);
		verifyNoInteractions(telegramBot, kafkaProducer, kafkaProperties, messageUtils);
	}
	@Test
	void processUpdate_WithTextMessage(){
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		when(update.getMessage()).thenReturn(message);
		when(message.hasText()).thenReturn(true);
		when(kafkaProperties.getText_message()).thenReturn("text_message");
		updateService.processUpdate(update);
		verify(kafkaProducer).produce("text_message", update);
		verifyNoMoreInteractions(telegramBot, kafkaProducer, messageUtils);
	}
	@Test
	void processUpdate_WithDocMessage(){
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		when(update.getMessage()).thenReturn(message);
		when(message.hasDocument()).thenReturn(true);
		when(kafkaProperties.getDoc_message()).thenReturn("doc_message");
		SendMessage sendMessage = new SendMessage();
		when(messageUtils.generateResponseMessage(update,"Файл обрабатывается...")).thenReturn(sendMessage);
		updateService.processUpdate(update);
		verify(kafkaProducer).produce("doc_message", update);
		verify(telegramBot).sendAnswerMessage(sendMessage);
	}
	@Test
	void processUpdate_WithPhotoMessage(){
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		when(update.getMessage()).thenReturn(message);
		when(message.hasPhoto()).thenReturn(true);
		when(kafkaProperties.getPhoto_message()).thenReturn("photo_message");
		SendMessage sendMessage = new SendMessage();
		when(messageUtils.generateResponseMessage(update, "Файл обрабатывается...")).thenReturn(sendMessage);
		updateService.processUpdate(update);
		verify(kafkaProducer).produce("photo_message", update);
		verify(telegramBot).sendAnswerMessage(sendMessage);
	}
	@Test
	void processUpdate_UnsupportedMessage(){
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		when(update.getMessage()).thenReturn(message);
		when(message.hasText()).thenReturn(false);
		when(message.hasDocument()).thenReturn(false);
		when(message.hasPhoto()).thenReturn(false);
		SendMessage sendMessage = new SendMessage();
		when(messageUtils.generateResponseMessage(update, "Неподдерживаемый тип сообщения"))
				.thenReturn(sendMessage);
		updateService.processUpdate(update);
		verify(telegramBot).sendAnswerMessage(sendMessage);
	}

}