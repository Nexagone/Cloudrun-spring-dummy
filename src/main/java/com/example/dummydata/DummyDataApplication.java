package com.example.dummydata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DummyDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DummyDataApplication.class, args);
    }
}
