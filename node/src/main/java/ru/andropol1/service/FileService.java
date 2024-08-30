package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.enums.LinkType;

public interface FileService {
	AppDocument processDoc(Message message);
	AppPhoto processPhoto(Message message);
	String generateLink(Long docId, LinkType linkType);
}
