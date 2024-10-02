package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.andropol1.dto.MailParams;
import ru.andropol1.service.MailSenderService;
@Service
@Log4j
public class MailSenderServiceImpl implements MailSenderService {
	private final JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String emailFrom;
	@Value("${service.activation.uri}")
	private String activationServiceUri;
	@Autowired

	public MailSenderServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void send(MailParams mailParams) {
		String subject = "Активация учетной записи";
		String body = getActivationMailBody(mailParams.getId());
		String emailTo = mailParams.getEmailTo();
		log.debug(String.format("Sending email for mail=[%s]", emailTo));
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(emailFrom);
		mailMessage.setTo(emailTo);
		mailMessage.setSubject(subject);
		mailMessage.setText(body);
		javaMailSender.send(mailMessage);
	}

	private String getActivationMailBody(String id) {
		String msg = String.format("Для завершения регистрации перейдите по ссылке: \n%s",
				activationServiceUri);
		return msg.replace("{id}", id);
	}
}
