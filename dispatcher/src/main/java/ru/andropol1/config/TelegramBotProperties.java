package ru.andropol1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("bot")
@Data
@Component
public class TelegramBotProperties {
	private String name;
	private String token;
	private String uri;
}
