package main.lab1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// есть интерфейс для сторинга даты? в 1 лабе у нас просто мапа/массив,
// потом мы будем наследовать этот интерфейс для другого хранения(Postgresql)
//Spring version 3.4.3
@SpringBootApplication
public class Lab1Application {

	public static void main(String[] args) {
		try {
			SpringApplication.run(Lab1Application.class, args);
		} catch (Exception e) {
			System.out.println("See logs");
		}

	}
}
