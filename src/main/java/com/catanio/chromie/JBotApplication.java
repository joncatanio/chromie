package com.catanio.chromie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "com.catanio.chromie"})
public class JBotApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Entry point of the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(JBotApplication.class, args);
    }
}
