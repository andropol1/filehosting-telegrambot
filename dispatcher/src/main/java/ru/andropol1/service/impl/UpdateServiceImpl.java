package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.controller.TelegramBot;
import ru.andropol1.service.UpdateService;
import ru.andropol1.utils.MessageUtils;

@Service
@Log4j
public class UpdateServiceImpl implements UpdateService {
	private final MessageUtils messageUtils;
	private final TelegramBot telegramBot;
	@Autowired

	public UpdateServiceImpl(MessageUtils messageUtils, TelegramBot telegramBot) {
		this.messageUtils = messageUtils;
		this.telegramBot = telegramBot;
	}

	@Override
	public void processUpdate(Update update) {
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
	private void filterMessagesByContent(Update update) {
		Message message = update.getMessage();
		if (message.hasText()){
			messageUtils.processTextMessage(update);
		} else if (message.hasDocument()) {
			setFileIsReceived(update);
			messageUtils.processDocMessage(update);
		} else if (message.hasPhoto()) {
			setFileIsReceived(update);
			messageUtils.processPhotoMessage(update);
		} else {
			setUnsupportedMessage(update);
		}
	}

	private void setUnsupportedMessage(Update update) {
		SendMessage responseMessage = messageUtils.generateResponseMessage(update, "Неподдерживаемый тип сообщения");
		telegramBot.sendAnswerMessage(responseMessage);
	}
	private void setFileIsReceived(Update update){
		SendMessage responseMessage = messageUtils.generateResponseMessage(update, "Файл обрабатывается...");
		telegramBot.sendAnswerMessage(responseMessage);
	}
}
