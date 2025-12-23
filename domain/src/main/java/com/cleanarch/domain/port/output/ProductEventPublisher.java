package com.cleanarch.domain.port.output;

import com.cleanarch.domain.model.Product;

/**
 * Output Port for Messaging (Kafka Producer) - Hexagonal Architecture
 * This interface defines how to send messages to external systems
 */
public interface ProductEventPublisher {
    
    void publishProductCreated(Product product);
    
    void publishProductUpdated(Product product);
    
    void publishProductDeleted(Long productId);
}
