package com.cleanarch.infrastructure.config;

import com.cleanarch.application.port.input.ProductUseCase;
import com.cleanarch.application.service.ProductService;
import com.cleanarch.domain.port.output.ProductEventPublisher;
import com.cleanarch.domain.port.output.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for application layer beans
 * This wires up the hexagonal architecture
 */
@Configuration
public class ApplicationConfig {
    
    @Bean
    public ProductUseCase productUseCase(
            ProductRepository productRepository,
            ProductEventPublisher eventPublisher) {
        return new ProductService(productRepository, eventPublisher);
    }
}
