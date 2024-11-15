package ru.andropol1.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerImplTest {
	@Mock
	KafkaTemplate kafkaTemplate;
	@InjectMocks
	KafkaProducerImpl kafkaProducer;
	@Test
	void produceUpdateSuccess(){
		String topic = "test";
		Update update = mock(Update.class);
		Message message = mock(Message.class);
		when(update.getMessage()).thenReturn(message);
		when(message.getText()).thenReturn("Test");
		kafkaProducer.produce(topic, update);
		verify(kafkaTemplate).send(topic, update);
	}

}