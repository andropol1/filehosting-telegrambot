package ru.andropol1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hash")
@Data
public class HashProperties {
	private String salt;
	private int minHashLength;
}
