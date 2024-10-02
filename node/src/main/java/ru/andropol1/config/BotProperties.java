package ru.andropol1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("bot")
@Component
public class BotProperties{
	private String token;
	private String file_info_uri;
	private String file_storage_uri;
	private String linkAddress;
}
