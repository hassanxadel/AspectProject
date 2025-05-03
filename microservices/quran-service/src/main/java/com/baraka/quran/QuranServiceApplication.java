package com.baraka.quran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QuranServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuranServiceApplication.class, args);
    }
} 