package com.hexagonal.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot Application class.
 */
@SpringBootApplication(scanBasePackages = "com.hexagonal")
@EnableJpaRepositories(basePackages = "com.hexagonal.infrastructure.adapter.persistence")
public class ProductManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementApplication.class, args);
    }
}
