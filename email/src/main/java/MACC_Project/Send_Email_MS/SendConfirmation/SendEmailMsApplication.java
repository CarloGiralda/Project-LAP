package MACC_Project.Send_Email_MS.SendConfirmation;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SendEmailMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendEmailMsApplication.class, args);
	}

}

