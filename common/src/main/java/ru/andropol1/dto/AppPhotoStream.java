package ru.andropol1.dto;

import lombok.*;

import java.io.InputStream;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppPhotoStream {
	private InputStream inputStream;
}
