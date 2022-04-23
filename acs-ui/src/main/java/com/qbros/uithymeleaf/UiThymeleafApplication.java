package com.qbros.uithymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UiThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiThymeleafApplication.class, args);
    }

}
