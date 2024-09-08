package ru.andropol1.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
	@Bean
	public KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}

	@Bean
	public KafkaTemplate<String, Update> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ProducerFactory<String, Update> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public ConsumerFactory<String, Update> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "telegram");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		DefaultKafkaConsumerFactory<String, Update> factory = new DefaultKafkaConsumerFactory<>(props);
		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Update> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Update> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setMessageConverter(new StringJsonMessageConverter());
		return factory;
	}

	@Bean
	public NewTopic docMessageTopic() {
		return new NewTopic(kafkaProperties().getDoc_message(), 1, (short) 1);
	}

	@Bean
	public NewTopic textMessageTopic() {
		return new NewTopic(kafkaProperties().getText_message(), 1, (short) 1);
	}

	@Bean
	public NewTopic photoMessageTopic() {
		return new NewTopic(kafkaProperties().getPhoto_message(), 1, (short) 1);
	}

	@Bean
	public NewTopic answerMessageTopic() {
		return new NewTopic(kafkaProperties().getAnswer_message(), 1, (short) 1);
	}
}
