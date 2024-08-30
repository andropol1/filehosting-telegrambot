package ru.andropol1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("decoder")
@Data
public class DecoderProperties {
	private String salt;
	private int minHashLength;
}
