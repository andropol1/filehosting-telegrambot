package ru.andropol1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.kafka")
@Data
public class KafkaProperties {
	private String doc_message;
	private String photo_message;
	private String text_message;
	private String answer_message;
}
