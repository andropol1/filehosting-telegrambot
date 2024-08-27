package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;

public interface FIleService {
	AppDocument processDoc(Message message);
	AppPhoto processPhoto(Message message);
}
