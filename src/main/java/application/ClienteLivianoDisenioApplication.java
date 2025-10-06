package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"controllers"
})
public class ClienteLivianoDisenioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteLivianoDisenioApplication.class, args);
	}

}
