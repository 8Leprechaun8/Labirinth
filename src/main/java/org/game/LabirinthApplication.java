package org.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LabirinthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabirinthApplication.class);
    }
}