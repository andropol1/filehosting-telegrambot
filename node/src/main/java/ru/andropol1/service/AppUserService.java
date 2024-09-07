package ru.andropol1.service;

import ru.andropol1.entity.AppUser;

public interface AppUserService {
	String registerUser(AppUser appUser);
	String setEmail(AppUser appUser, String email);
}
