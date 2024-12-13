package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.andropol1.entity.AppUser;
import ru.andropol1.service.KafkaProducer;
@Service
@Log4j
public class KafkaProducerImpl implements KafkaProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	@Value("${spring.kafka.answer_message}")
	private String answerMessage;
	@Autowired
	public KafkaProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public void produceSuccessRegistration(AppUser appUser) {
		String answer = "Вы успешно зарегистрировались! Теперь вы можете загружать любые ваши документы и фото, " +
				"а также получать доступ к ним по ссылке.";
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(appUser.getTelegramUserId());
		sendMessage.setText(answer);
		kafkaTemplate.send(answerMessage, sendMessage);
	}
}
