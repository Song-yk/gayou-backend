package com.gayou.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import jakarta.annotation.PostConstruct;

@Configuration
public class DotenvConfig {

    private final ConfigurableEnvironment environment;

    public DotenvConfig(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        // Dotenv dotenv = Dotenv.load();
        Dotenv dotenv = Dotenv.configure().directory(System.getProperty("user.dir") + "/gayou-backend").load();
        // Dotenv로 불러온 값을 Spring 환경 변수에 추가
        dotenv.entries().forEach(entry -> {
            environment.getSystemProperties().put(entry.getKey(), entry.getValue());
        });
    }
}
