package main.lab1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.boot.test.web.client.TestRestTemplate;


@SpringBootTest
@ActiveProfiles("internal")
class Lab1ApplicationTests {
	@Test
	void contextLoads()  throws Exception{
	}
}
