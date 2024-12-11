package ru.andropol1.service;

import ru.andropol1.dto.AppDocumentStream;
import ru.andropol1.dto.AppPhotoStream;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.entity.BinaryContent;

import java.util.Optional;

public interface FileService {
	Optional<AppDocumentStream> getDocument(String id);
	Optional<AppPhotoStream> getPhoto(String id);
}
