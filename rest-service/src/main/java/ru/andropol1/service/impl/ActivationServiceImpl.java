package ru.andropol1.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.andropol1.entity.AppUser;
import ru.andropol1.repository.AppUserRepository;
import ru.andropol1.service.ActivationService;
import ru.andropol1.utils.Decoder;

import java.util.Optional;

@Service
@Log4j
public class ActivationServiceImpl implements ActivationService {
	private final AppUserRepository appUserRepository;
	private final Decoder decoder;

	public ActivationServiceImpl(AppUserRepository appUserRepository, Decoder decoder) {
		this.appUserRepository = appUserRepository;
		this.decoder = decoder;
	}

	@Override
	public boolean activation(String hashId) {
		Long id = decoder.idOf(hashId);
		log.debug(String.format("User activation with user id=%s", id));
		Optional<AppUser> optional = appUserRepository.findById(id);
		if (optional.isPresent()){
			AppUser user = optional.get();
			user.setIsActive(true);
			appUserRepository.save(user);
			return true;
		}
		return false;
	}
}
