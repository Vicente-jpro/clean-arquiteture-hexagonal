package com.cleanarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point
 * This is the only place where we have Spring Boot framework dependency
 */
@SpringBootApplication
public class ProductManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductManagementApplication.class, args);
    }
}
