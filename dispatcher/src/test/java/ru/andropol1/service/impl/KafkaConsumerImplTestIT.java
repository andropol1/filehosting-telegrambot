package ru.andropol1.service.impl;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.andropol1.JUnitSpringBootBase;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
@TestPropertySource(properties = {
		"spring.kafka.bootstrap-servers=${kafka.bootstrapAddress}",
})
@WireMockTest(httpPort = 54321)

class KafkaConsumerImplTestIT extends JUnitSpringBootBase {
	@Autowired
	KafkaTemplate<String, SendMessage> kafkaTemplate;
	@Container
	static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));
	@BeforeAll
	static void setup() {
		kafkaContainer.start();
		System.setProperty("kafka.bootstrapAddress", kafkaContainer.getBootstrapServers());
	}
	@Test
	void consumeSuccess(){
		WireMock.stubFor(WireMock.post(WireMock.urlPathMatching("/sendmessage"))
								 .willReturn(aResponse().withStatus(200)));
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId("1");
		sendMessage.setText("test");
		kafkaTemplate.send("answer_message", sendMessage);
		await().atMost(5, SECONDS).untilAsserted(() ->
				WireMock.verify(postRequestedFor(urlPathMatching("/sendmessage"))
						.withRequestBody(containing("test")))
		);
	}
}