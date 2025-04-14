package main.lab1;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
//Springboot version 3.4.3
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
//@EnableTransactionManagement
//@EnableKafka //can be omitted with my default kafka setup
public class Lab1Application {

	public static void main(String[] args) {
		try {
			SpringApplication.run(Lab1Application.class, args);
		} catch (Exception e) {
			System.out.println("See logs");
		}
	
	}


	@KafkaListener(id = "myId", topics = "topic1")
	public void listen(String in) {
		System.out.println(in);
	}
}
