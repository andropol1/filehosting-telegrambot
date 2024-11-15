package ru.andropol1.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class KafkaConfiguration {
	@Bean
	public KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}

}
