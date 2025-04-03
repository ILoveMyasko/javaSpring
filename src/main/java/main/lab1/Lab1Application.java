package main.lab1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//Spring version 3.4.3
@SpringBootApplication
@EnableCaching
public class Lab1Application {

	public static void main(String[] args) {
		try {
			SpringApplication.run(Lab1Application.class, args);
		} catch (Exception e) {
			System.out.println("See logs");
		}
	
	}
}
