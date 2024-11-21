package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.andropol1.dto.MailParams;
import ru.andropol1.entity.AppUser;
import ru.andropol1.repository.AppUserRepository;
import ru.andropol1.service.AppUserService;
import ru.andropol1.service.KafkaProducer;

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
	private final KafkaProducer kafkaProducer;

	@Autowired
	public AppUserServiceImpl(AppUserRepository appUserRepository, Hashids hashids, KafkaProducer kafkaProducer) {
		this.appUserRepository = appUserRepository;
		this.hashids = hashids;
		this.kafkaProducer = kafkaProducer;
	}

	@Override
	public String registerUser(AppUser appUser) {
		if (appUser.getIsActive()){
			return "Вы уже зарегистрированы!";
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
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();

		} catch (AddressException e) {
			return "Введите, пожалуйста, корректный email. Для отмены команды введите /cancel";
		}
		Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
		if (optionalAppUser.isEmpty()){
			appUser.setEmail(email);
			appUser.setUserState(BASIC_STATE);
			appUserRepository.save(appUser);
			String hashId = hashids.encode(appUser.getId());
			sendRequestToMailService(hashId, email);
			//TODO Добавить проверку получения mail-service сообщения
			return "Вам на почту было отправлено письмо."
						+ "Перейдите по ссылке в письме для подтверждения регистрации.";
		} else {
			return "Этот email уже используется. Введите корректный email."
					+ " Для отмены команды введите /cancel";
		}
	}

	@Override
	public AppUser findOrSaveAppUser(Update update) {
		User user = update.getMessage().getFrom();
		Optional<AppUser> persistentUser = appUserRepository.findByTelegramUserId(user.getId());
		if (persistentUser.isEmpty()){
			AppUser transientUser = AppUser.builder()
										   .telegramUserId(user.getId())
										   .userName(user.getUserName())
										   .firstName(user.getFirstName())
										   .lastName(user.getLastName())
										   .isActive(false)
										   .userState(BASIC_STATE)
										   .build();
			return appUserRepository.save(transientUser);
		}
		return persistentUser.get();
	}

	private void sendRequestToMailService(String hashId, String email) {
		MailParams mailParams = MailParams.builder()
				.id(hashId)
				.emailTo(email)
				.build();
		kafkaProducer.produceRegistrationMessage(mailParams);
	}
}
