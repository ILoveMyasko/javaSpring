package main.lab1;

import main.lab1.kafkaEvents.TaskEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


//@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@SpringBootTest
@ActiveProfiles("h2")
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
class Lab1ApplicationTests {
	@MockitoBean
	private KafkaTemplate<String, TaskEvent> kafkaTemplate;

	@Test
	void contextLoads()  throws Exception{
	}
}
