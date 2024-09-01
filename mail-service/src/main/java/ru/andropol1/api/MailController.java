package ru.andropol1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.andropol1.dto.MailParams;
import ru.andropol1.service.MailSenderService;

@RestController
@RequestMapping("/mail")
public class MailController {
	private final MailSenderService mailSenderService;
	@Autowired

	public MailController(MailSenderService mailSenderService) {
		this.mailSenderService = mailSenderService;
	}
	@PostMapping("/send")
	public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams){
		mailSenderService.send(mailParams);
		return ResponseEntity.ok().build();
	}
}
