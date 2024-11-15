package ru.andropol1.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.andropol1.controller.TelegramBot;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerImplTest {
	@Mock
	TelegramBot telegramBot;
	@InjectMocks
	KafkaConsumerImpl kafkaConsumer;
	@Test
	void consumeSendMessageSuccess(){
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId("1");
		sendMessage.setText("Test");
		kafkaConsumer.consume(sendMessage);
		verify(telegramBot).sendAnswerMessage(sendMessage);
	}

}