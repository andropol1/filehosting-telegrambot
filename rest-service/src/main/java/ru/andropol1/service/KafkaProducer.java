package ru.andropol1.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.entity.AppUser;

public interface KafkaProducer {
	void produceSuccessRegistration(AppUser appUser);
}
