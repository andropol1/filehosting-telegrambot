package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.entity.BinaryContent;
import ru.andropol1.repository.AppDocumentRepository;
import ru.andropol1.repository.AppPhotoRepository;
import ru.andropol1.service.FileService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Log4j
public class FileServiceImpl implements FileService {
	private final AppDocumentRepository appDocumentRepository;
	private final AppPhotoRepository appPhotoRepository;
	@Autowired
	public FileServiceImpl(AppDocumentRepository appDocumentRepository, AppPhotoRepository appPhotoRepository) {
		this.appDocumentRepository = appDocumentRepository;
		this.appPhotoRepository = appPhotoRepository;
	}

	@Override
	public Optional<AppDocument> getDocument(String docId) {
		long id = Long.parseLong(docId);
		return appDocumentRepository.findById(id);
	}

	@Override
	public Optional<AppPhoto> getPhoto(String photoId) {
		long id = Long.parseLong(photoId);
		return appPhotoRepository.findById(id);
	}

	@Override
	public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
		try{
			File tempFile = File.createTempFile("tempFile", ".bin");
			tempFile.deleteOnExit();
			FileUtils.writeByteArrayToFile(tempFile, binaryContent.getFileAsArrayOfBytes());
			return new FileSystemResource(tempFile);
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
}
