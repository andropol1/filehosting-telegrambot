package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.andropol1.config.BotProperties;
import ru.andropol1.dto.MailParams;
import ru.andropol1.entity.AppUser;
import ru.andropol1.repository.AppUserRepository;
import ru.andropol1.service.AppUserService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.Optional;

import static ru.andropol1.enums.UserState.BASIC_STATE;
import static ru.andropol1.enums.UserState.WAIT_FOR_EMAIL_STATE;

@Service
@Log4j
public class AppUserServiceImpl implements AppUserService {
	private final AppUserRepository appUserRepository;
	private final Hashids hashids;
	private final BotProperties botProperties;
	private final WebClient webClient;

	@Autowired
	public AppUserServiceImpl(AppUserRepository appUserRepository, Hashids hashids, BotProperties botProperties) {
		this.appUserRepository = appUserRepository;
		this.hashids = hashids;
		this.botProperties = botProperties;
		this.webClient = WebClient.create();
	}

	@Override
	public String registerUser(AppUser appUser) {
		if (appUser.getIsActive()){
			return "Вы уже зарегестрированы!";
		} else if (appUser.getEmail() != null){
			return "Вам на почту уже было отправлено письмо. "
					+ "Перейдите по ссылке в письме для подтверждения регистрации.";
		}
		appUser.setUserState(WAIT_FOR_EMAIL_STATE);
		appUserRepository.save(appUser);
		return "Введите пожалуйста ваш email";
	}

	@Override
	public String setEmail(AppUser appUser, String email) {
		try{
			InternetAddress emailAddrs = new InternetAddress(email);
			emailAddrs.validate();

		} catch (AddressException e) {
			return "Введите, пожалуйста, корректный email. Для отмены команды введите /cancel";
		}
		Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
		if (optionalAppUser.isEmpty()){
			appUser.setEmail(email);
			appUser.setUserState(BASIC_STATE);
			appUserRepository.save(appUser);
			String hashId = hashids.encode(appUser.getId());
			ResponseEntity<String> response = sendRequestToMailService(hashId, email);
			if (response.getStatusCode() != HttpStatus.OK){
				String msg = String.format("Отправка эл. письма на почту %s не удалась", email);
				log.error(msg);
				appUser.setEmail(null);
				appUserRepository.save(appUser);
				return msg;
			} else {
				return "Вам на почту было отправлено письмо."
						+ "Перейдите по ссылке в письме для подтверждения регистрации.";
			}
		} else {
			return "Этот email уже используется. Введите корректный email."
					+ " Для отмены команды введите /cancel";
		}
	}

	private ResponseEntity<String> sendRequestToMailService(String hashId, String email) {
		MailParams mailParams = MailParams.builder()
				.id(hashId)
				.emailTo(email)
				.build();
		return webClient.post()
				.uri(botProperties.getMail_uri())
				.bodyValue(mailParams)
				.retrieve()
				.toEntity(String.class)
				.block();
	}
}
