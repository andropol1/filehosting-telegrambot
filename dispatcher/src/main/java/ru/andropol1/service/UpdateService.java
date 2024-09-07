package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateService {
	void processUpdate(Update update);
}
