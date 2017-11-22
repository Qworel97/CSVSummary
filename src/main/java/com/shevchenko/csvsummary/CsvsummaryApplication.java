package com.shevchenko.csvsummary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CsvsummaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvsummaryApplication.class, args);
    }
}
