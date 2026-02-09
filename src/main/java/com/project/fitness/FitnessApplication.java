package com.project.fitness;

import com.project.fitness.config.DotenvInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FitnessApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FitnessApplication.class)
                .initializers(new DotenvInitializer())
                .run(args);
    }

}
