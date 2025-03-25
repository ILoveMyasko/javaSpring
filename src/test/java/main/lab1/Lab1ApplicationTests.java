package main.lab1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;


@SpringBootTest
class Lab1ApplicationTests {

//	@Autowired //controller injected?
//	private UserController controller;
	//@Autowired
	//private TestRestTemplate restTemplate;
	@Test
	void contextLoads()  throws Exception{
		System.out.println("im here?");
		//assertThat(controller).isNotNull();
	}

}
