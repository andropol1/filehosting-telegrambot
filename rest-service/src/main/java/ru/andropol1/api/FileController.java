package ru.andropol1.api;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.andropol1.entity.BinaryContent;
import ru.andropol1.exceptions.ResourceNotFoundException;
import ru.andropol1.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
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
	public void getDoc(@RequestParam String id, HttpServletResponse response) {
		fileService.getDocument(id)
				   .ifPresentOrElse(doc -> {
							   response.setContentType(MediaType.parseMediaType(doc.getMimeType())
																.toString());
							   response.setHeader("Content-disposition",
									   "attachment; filename:=" + doc.getDocName());
							   response.setStatus(HttpServletResponse.SC_OK);
							   BinaryContent binaryContent = doc.getBinaryContent();
							   try (OutputStream out = response.getOutputStream()) {
								   out.write(binaryContent.getFileAsArrayOfBytes());
							   } catch (IOException e) {
								   throw new UncheckedIOException(e);
							   }
						   },
						   () -> {
							   throw new ResourceNotFoundException("Document not found for id: " + id);
						   });

	}

	@GetMapping("/get-photo")
	public void getPhoto(@RequestParam String id, HttpServletResponse response) {
		fileService.getPhoto(id)
				   .ifPresentOrElse(photo  -> {
							   response.setContentType(MediaType.IMAGE_JPEG.toString());
							   response.setHeader("Content-disposition", "attachment;");
							   response.setStatus(HttpServletResponse.SC_OK);
							   BinaryContent binaryContent = photo.getBinaryContent();
							   try (OutputStream out = response.getOutputStream()) {
								   out.write(binaryContent.getFileAsArrayOfBytes());
							   } catch (IOException e) {
								   throw new UncheckedIOException(e);
							   }
						   },
						   () -> {
							   throw new ResourceNotFoundException("Photo not found for id: " + id);
						   });
	}
}
