package com.catanio.chromie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "com.catanio.chromie"})
@EntityScan(basePackages = "com.catanio.chromie.dm.entities")
@EnableJpaRepositories("com.catanio.chromie.dm.repositories")
public class ChromieApplication {
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
        SpringApplication.run(ChromieApplication.class, args);
    }
}
