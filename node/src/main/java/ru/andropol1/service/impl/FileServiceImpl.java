package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.hashids.Hashids;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.andropol1.config.BotProperties;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.AppPhoto;
import ru.andropol1.entity.BinaryContent;
import ru.andropol1.enums.LinkType;
import ru.andropol1.exceptions.UploadFileException;
import ru.andropol1.repository.AppDocumentRepository;
import ru.andropol1.repository.AppPhotoRepository;
import ru.andropol1.repository.BinaryContentRepository;
import ru.andropol1.service.FileService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Log4j
@Service
public class FileServiceImpl implements FileService {
	private final AppDocumentRepository appDocumentRepository;
	private final AppPhotoRepository appPhotoRepository;
	private final BinaryContentRepository binaryContentRepository;
	private final BotProperties botProperties;
	private final WebClient webClient;
	private final Hashids hashids;
	@Autowired
	public FileServiceImpl(AppDocumentRepository appDocumentRepository, AppPhotoRepository appPhotoRepository,
						   BinaryContentRepository binaryContentRepository,
						   BotProperties botProperties, Hashids hashids) {
		this.appDocumentRepository = appDocumentRepository;
		this.appPhotoRepository = appPhotoRepository;
		this.binaryContentRepository = binaryContentRepository;
		this.botProperties = botProperties;
		this.hashids = hashids;
		this.webClient = WebClient.create();
	}

	@Override
	public AppDocument processDoc(Message message) {
		Document document = message.getDocument();
		String fileId = message.getDocument().getFileId();
		ResponseEntity<String> response = getFilePath(fileId);
		if (response.getStatusCode() == HttpStatus.OK){
			BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
			AppDocument transientAppDoc = buildTransientAppDoc(document, persistentBinaryContent);
			return appDocumentRepository.save(transientAppDoc);
		} else {
			throw new UploadFileException("Bad response from telegram service: " + response);
		}
	}

	@Override
	public AppPhoto processPhoto(Message message) {
		int photoSize = message.getPhoto().size();
		int photoIndex = photoSize > 1 ? message.getPhoto().size() - 1 : 0;
		PhotoSize photo = message.getPhoto().get(photoIndex);
		String fileId = photo.getFileId();
		ResponseEntity<String> response = getFilePath(fileId);
		if (response.getStatusCode() == HttpStatus.OK){
			BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
			AppPhoto transientAppPhoto = buildTransientAppPhoto(photo, persistentBinaryContent);
			return appPhotoRepository.save(transientAppPhoto);
		} else {
			throw new UploadFileException("Bad response from telegram service: " + response);
		}
	}

	@Override
	public String generateLink(Long docId, LinkType linkType) {
		String hash = hashids.encode(docId);
		return "http://" + botProperties.getLinkAddress() +  "/" + linkType + "?id=" + hash;
	}

	private AppPhoto buildTransientAppPhoto(PhotoSize photo, BinaryContent persistentBinaryContent) {
		return AppPhoto.builder()
				.telegramFileId(photo.getFileId())
				.binaryContent(persistentBinaryContent)
				.fileSize(photo.getFileSize())
				.build();
	}

	private BinaryContent getPersistentBinaryContent(ResponseEntity<String> response){
		JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.getBody()));
		String filePath = jsonObject.getJSONObject("result").getString("file_path");
		byte[] fileInByte = downloadFile(filePath);
		BinaryContent transientBinaryContent = BinaryContent.builder()
				.fileAsArrayOfBytes(fileInByte)
				.build();
		return binaryContentRepository.save(transientBinaryContent);

	}

	private AppDocument buildTransientAppDoc(Document document, BinaryContent persistentBinaryContent) {
		return AppDocument.builder().
				telegramFileId(document.getFileId())
				.docName(document.getFileName())
				.binaryContent(persistentBinaryContent)
				.mimeType(document.getMimeType())
				.fileSize(document.getFileSize())
				.build();
	}

	private byte[] downloadFile(String filePath) {
		String fullURI = botProperties.getFile_storage_uri().replace("{token}", botProperties.getToken())
				.replace("{filePath}", filePath);
		URL urlObj = null;
		try{
			urlObj = new URL(fullURI);
		} catch (MalformedURLException e) {
			throw new UploadFileException(e);
		}
		try(InputStream inputStream = urlObj.openStream()){
			return inputStream.readAllBytes();
		} catch (IOException e) {
			throw new UploadFileException(urlObj.toExternalForm(), e);
		}
	}

	private ResponseEntity<String> getFilePath(String fileId) {
		return 	webClient
				.get()
				.uri(botProperties.getFile_info_uri().replace("{token}", botProperties.getToken())
						.replace("{fileId}", fileId))
				.retrieve()
				.toEntity(String.class)
				.block();
	}
}
