package ru.andropol1.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MailParams {
	private String id;
	private String emailTo;
}
