package ru.andropol1.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import ru.andropol1.dto.MailParams;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
	@Bean
	public KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}
	@Bean
	public ConsumerFactory<String, MailParams> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9093");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "telegram");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		DefaultKafkaConsumerFactory<String, MailParams> factory = new DefaultKafkaConsumerFactory<>(props);
		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, MailParams> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, MailParams> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setMessageConverter(new StringJsonMessageConverter());
		return factory;
	}
	@Bean
	public NewTopic registrationMessageTopic() {
		return new NewTopic(kafkaProperties().getRegistration_message(), 1, (short) 1);
	}
}
