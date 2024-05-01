package deti.tqs.phihub;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import deti.tqs.phihub.configs.Generated;

@Generated
@SpringBootApplication
public class PhiHubApplication {
	public static void main(String[] args) {
		SpringApplication.run(PhiHubApplication.class, args);
	}

}
