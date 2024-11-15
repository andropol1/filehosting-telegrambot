package ru.andropol1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import ru.andropol1.JUnitSpringBootBase;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@TestPropertySource(properties = {
		"spring.kafka.bootstrap-servers=${kafka.bootstrapAddress}"
})
class WebHookControllerTestIT extends JUnitSpringBootBase {
	@Autowired
	MockMvc mockMvc;
	@Container
	static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));

	static  {
		kafkaContainer.start();
		System.setProperty("kafka.bootstrapAddress", kafkaContainer.getBootstrapServers());
	}

	@Test
	void onUpdateReceivedSuccess() throws Exception {
		Update update = new Update();
		Message message = new Message();
		message.setText("Test");
		update.setMessage(message);
		String jsonUpdate = new ObjectMapper().writeValueAsString(update);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/callback/update")
																			 .contentType("application/json")
																			 .content(jsonUpdate);
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk());
	}
}