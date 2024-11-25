package ru.andropol1.exceptions;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.UncheckedIOException;

@Log4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							 .body(ex.getMessage());
	}

	@ExceptionHandler(UncheckedIOException.class)
	public ResponseEntity<String> handleIOException(IOException ex) {
		log.error(ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							 .body("An internal error occurred while processing the request. Please try again later.");
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception ex){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An unexpected error occurred");
	}

}
