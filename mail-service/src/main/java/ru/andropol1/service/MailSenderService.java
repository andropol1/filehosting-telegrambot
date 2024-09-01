package ru.andropol1.service;

import ru.andropol1.dto.MailParams;

public interface MailSenderService {
	void send(MailParams mailParams);
}
