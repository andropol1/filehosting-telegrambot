package ru.andropol1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.andropol1.config.TelegramBotProperties;

@SpringBootApplication
@EnableConfigurationProperties(TelegramBotProperties.class)
public class DispatcherApplication {
	public static void main(String[] args) {

		SpringApplication.run(DispatcherApplication.class);
	}
}