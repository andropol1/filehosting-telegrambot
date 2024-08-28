package ru.andropol1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kafka")
@Data
public class KafkaProperties {
	private String doc_message = "doc_message";
	private String photo_message = "photo_message";
	private String text_message = "text_message";
	private String answer_message = "answer_message";
}
