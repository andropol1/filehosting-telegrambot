package ru.andropol1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bot")
@Data
public class TelegramBotProperties {
	private String name;
	private String token;
}
