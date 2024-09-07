package ru.andropol1.service;

import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;

import java.util.Optional;

public interface FileService {
	Optional<AppDocument> getDocument(String id);
	Optional<AppPhoto> getPhoto(String id);
}
