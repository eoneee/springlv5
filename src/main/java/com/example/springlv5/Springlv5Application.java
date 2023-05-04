package com.example.springlv5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Springlv5Application {

    public static void main(String[] args) {
        SpringApplication.run(Springlv5Application.class, args);
    }

}
