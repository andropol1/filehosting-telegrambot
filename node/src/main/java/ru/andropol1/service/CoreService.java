package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.entity.AppUser;

public interface CoreService {
	void sendAnswer(String output, Long chatId);
	String processServiceCommand(AppUser appUser, String cmd);
	String cancelProcess(AppUser appUser);
	void saveUpdate(Update update);

}
