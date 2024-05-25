package deti.tqs.phihub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import deti.tqs.phihub.configs.Generated;

@Generated
@EnableAutoConfiguration
@SpringBootApplication
public class PhiHubApplication {
	public static void main(String[] args) {
		SpringApplication.run(PhiHubApplication.class, args);
	}

}
