package ru.andropol1.dto;

import lombok.*;

import java.io.InputStream;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppDocumentStream {
	private String docName;
	private String mimeType;
	private InputStream inputStream;
}
