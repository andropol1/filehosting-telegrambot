package ru.andropol1.service;

import org.springframework.core.io.FileSystemResource;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.entity.BinaryContent;

import java.util.Optional;

public interface FileService {
	Optional<AppDocument> getDocument(String id);
	Optional<AppPhoto> getPhoto(String id);
	FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
