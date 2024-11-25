package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.repository.AppDocumentRepository;
import ru.andropol1.repository.AppPhotoRepository;
import ru.andropol1.service.FileService;
import ru.andropol1.utils.Decoder;
import java.util.Optional;

@Service
@Log4j
public class FileServiceImpl implements FileService {
	private final AppDocumentRepository appDocumentRepository;
	private final AppPhotoRepository appPhotoRepository;
	private final Decoder decoder;
	@Autowired
	public FileServiceImpl(AppDocumentRepository appDocumentRepository, AppPhotoRepository appPhotoRepository, Decoder decoder) {
		this.appDocumentRepository = appDocumentRepository;
		this.appPhotoRepository = appPhotoRepository;
		this.decoder = decoder;
	}

	@Override
	public Optional<AppDocument> getDocument(String hash) {
		return Optional.ofNullable(decoder.idOf(hash))
				.flatMap(appDocumentRepository::findById);
	}

	@Override
	public Optional<AppPhoto> getPhoto(String hash) {
		return Optional.ofNullable(decoder.idOf(hash))
					   .flatMap(appPhotoRepository::findById);
	}
}
