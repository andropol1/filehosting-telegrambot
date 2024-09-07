package ru.andropol1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.andropol1.service.ActivationService;

@RestController
@RequestMapping("/user")
public class ActivationController {
	private final ActivationService activationService;

	@Autowired
	public ActivationController(ActivationService activationService) {
		this.activationService = activationService;
	}
	@GetMapping("/activation")
	public ResponseEntity<?> activation(@RequestParam String id){
		boolean activation = activationService.activation(id);
		if (activation){
			return ResponseEntity.ok().body("Регистрация успешно завершена!");
		}
		return ResponseEntity.internalServerError().build();
	}
}
