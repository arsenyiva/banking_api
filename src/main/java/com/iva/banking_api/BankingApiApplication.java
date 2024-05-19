package com.iva.banking_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Класс, представляющий основную точку входа для приложения Banking API.
 */
@SpringBootApplication
@EnableScheduling
public class BankingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApiApplication.class, args);
    }

}
