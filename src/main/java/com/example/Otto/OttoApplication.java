package com.example.Otto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The OttoApplication class is the entry point for the Otto application.
 * It configures Spring Boot application and enables JPA repositories.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.Otto.repository")
public class OttoApplication {

    /**
     * Main method to start the Otto application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(OttoApplication.class, args);
    }

}
