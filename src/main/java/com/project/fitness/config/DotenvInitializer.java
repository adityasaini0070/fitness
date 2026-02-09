package com.project.fitness.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.NonNull;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Map<String, Object> dotenvProperties = new HashMap<>();
        dotenv.entries().forEach(entry -> {
            dotenvProperties.put(entry.getKey(), entry.getValue());
            System.setProperty(entry.getKey(), entry.getValue());
        });

        environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", dotenvProperties));
    }
}
