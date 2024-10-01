package com.gayou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
public class GayouBackendApplication {
	public static void main(String[] args) {
		Dotenv dotenv;
		try {
			dotenv = Dotenv.load();
		} catch (Exception e) {
			dotenv = Dotenv.configure().directory(System.getProperty("user.dir") + "/gayou-backend").load();
		}
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});

		SpringApplication.run(GayouBackendApplication.class, args);
	}
}
