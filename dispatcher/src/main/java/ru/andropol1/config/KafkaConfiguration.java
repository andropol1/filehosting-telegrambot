package ru.andropol1.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {
	@Bean
	public KafkaProperties kafkaProperties(){
		return new KafkaProperties();
	}
	@Bean
	public NewTopic docMessageTopic(){
		return new NewTopic(kafkaProperties().getDoc_message(), 1, (short) 1);
	}
	@Bean
	public NewTopic textMessageTopic(){
		return new NewTopic(kafkaProperties().getText_message(), 1, (short) 1);
	}
	@Bean
	public NewTopic photoMessageTopic(){
		return new NewTopic(kafkaProperties().getPhoto_message(), 1, (short) 1);
	}
	@Bean
	public NewTopic answerMessageTopic(){
		return new NewTopic(kafkaProperties().getAnswer_message(), 1, (short) 1);
	}
}
