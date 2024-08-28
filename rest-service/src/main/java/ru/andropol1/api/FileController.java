package ru.andropol1.api;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.andropol1.entity.BinaryContent;
import ru.andropol1.service.FileService;

import java.util.Optional;

@Log4j
@RestController
@RequestMapping("/file")
public class FileController {
	private final FileService fileService;
	@Autowired
	public FileController(FileService fileService) {
		this.fileService = fileService;
	}
	@GetMapping("/get-doc")
	public ResponseEntity<?> getDoc(@RequestParam String id){
		return Optional.ofNullable(fileService.getDocument(id).get()).
				map(document -> {
					BinaryContent binaryContent = document.getBinaryContent();
					return Optional.ofNullable(fileService.getFileSystemResource(binaryContent))
							.map(fileSystemResource -> ResponseEntity.ok()
									.contentType(MediaType.parseMediaType(document.getMimeType()))
									.header("Content-disposition",
											"attachment; filename=" + document.getDocName())
									.body(fileSystemResource))
							.orElseGet(()->ResponseEntity.badRequest().build());
				})
				.orElseGet(()->ResponseEntity.badRequest().build());
	}
	@GetMapping("/get-photo")
	public ResponseEntity<?> getPhoto(@RequestParam String id){
		return Optional.ofNullable(fileService.getPhoto(id)).
				map(photo -> {
					BinaryContent binaryContent = photo.get().getBinaryContent();
					return Optional.ofNullable(fileService.getFileSystemResource(binaryContent))
							.map(fileSystemResource -> ResponseEntity.ok()
									.contentType(MediaType.IMAGE_JPEG)
									.header("Content-disposition", "attachment;")
									.body(fileSystemResource))
							.orElseGet(()->ResponseEntity.badRequest().build());
				})
				.orElseGet(()->ResponseEntity.badRequest().build());
	}
}
