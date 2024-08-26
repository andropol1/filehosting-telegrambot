package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.andropol1.config.BotProperties;
import ru.andropol1.entity.AppDocument;
import ru.andropol1.entity.BinaryContent;
import ru.andropol1.exceptions.UploadFileException;
import ru.andropol1.repository.AppDocumentRepository;
import ru.andropol1.repository.BinaryContentRepository;
import ru.andropol1.service.FIleService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Log4j
@Service
public class FIleServiceImpl implements FIleService {
	private final AppDocumentRepository appDocumentRepository;
	private final BinaryContentRepository binaryContentRepository;
	private final BotProperties botProperties;
	private final WebClient webClient;
	@Autowired
	public FIleServiceImpl(AppDocumentRepository appDocumentRepository, BinaryContentRepository binaryContentRepository, BotProperties botProperties) {
		this.appDocumentRepository = appDocumentRepository;
		this.binaryContentRepository = binaryContentRepository;
		this.botProperties = botProperties;
		this.webClient = WebClient.create();
	}

	@Override
	public AppDocument processDoc(Message message) {
		String fileId = message.getDocument().getFileId();
		ResponseEntity<String> response = getFilePath(fileId);
		if (response.getStatusCode() == HttpStatus.OK){
			JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.getBody()));
			String filePath = jsonObject.getJSONObject("result").getString("file_path");
			byte[] fileInByte = downloadFile(filePath);
			BinaryContent transientBinaryContent = BinaryContent.builder()
					.fileAsArrayOfBytes(fileInByte)
					.build();
			BinaryContent persistentBinaryContent = binaryContentRepository.save(transientBinaryContent);
			Document document = message.getDocument();
			AppDocument transientAppDoc = buildTransientAppDoc(document, persistentBinaryContent);
			return appDocumentRepository.save(transientAppDoc);
		} else {
			throw new UploadFileException("Bad response from telegram service: " + response);
		}
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
