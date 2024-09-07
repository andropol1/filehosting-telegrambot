package ru.andropol1.service.impl;

import org.springframework.stereotype.Service;
import ru.andropol1.entity.AppUser;
import ru.andropol1.repository.AppUserRepository;
import ru.andropol1.service.ActivationService;
import ru.andropol1.utils.Decoder;

import java.util.Optional;

@Service
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
