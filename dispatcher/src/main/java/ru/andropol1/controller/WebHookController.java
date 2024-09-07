package ru.andropol1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.service.UpdateService;

@RestController
public class WebHookController {
	private final UpdateService updateService;

	@Autowired

	public WebHookController(UpdateService updateService) {
		this.updateService = updateService;
	}
	@PostMapping("/callback/update")
	public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
		updateService.processUpdate(update);
		return ResponseEntity.ok().build();
	}
}
