package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.entity.AppUser;

public interface AppUserService {
	String registerUser(AppUser appUser);
	String setEmail(AppUser appUser, String email);
	AppUser findOrSaveAppUser(Update update);
	String resetEmail(AppUser appUser);
}
