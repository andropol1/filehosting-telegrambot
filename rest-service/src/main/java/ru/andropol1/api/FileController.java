package ru.andropol1.api;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.andropol1.entity.BinaryContent;
import ru.andropol1.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
	public void getDoc(@RequestParam String id, HttpServletResponse response){
		//TODO для формирования badRequest добавить ControllerAdvice
		Optional.ofNullable(fileService.getDocument(id).get())
				.ifPresentOrElse(doc -> {
					response.setContentType(MediaType.parseMediaType(doc.getMimeType()).toString());
					response.setHeader("Content-disposition", "attachment; filename:=" + doc.getDocName());
					response.setStatus(HttpServletResponse.SC_OK);
					BinaryContent binaryContent = doc.getBinaryContent();
					try(OutputStream out = response.getOutputStream()){
						out.write(binaryContent.getFileAsArrayOfBytes());
					} catch (IOException e){
						log.error(e);
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				},
						() -> response.setStatus(HttpServletResponse.SC_BAD_REQUEST));
	}
	@GetMapping("/get-photo")
	public void getPhoto(@RequestParam String id, HttpServletResponse response){
		Optional.ofNullable(fileService.getPhoto(id).get())
				.ifPresentOrElse(photo -> {
							response.setContentType(MediaType.IMAGE_JPEG.toString());
							response.setHeader("Content-disposition", "attachment;");
							response.setStatus(HttpServletResponse.SC_OK);
							BinaryContent binaryContent = photo.getBinaryContent();
							try(OutputStream out = response.getOutputStream()){
								out.write(binaryContent.getFileAsArrayOfBytes());
							} catch (IOException e){
								log.error(e);
								response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
							}
						},
						() -> response.setStatus(HttpServletResponse.SC_BAD_REQUEST));
	}
}
