package com.hexagonal.domain.port.out;

import com.hexagonal.domain.model.Product;

/**
 * Output port for messaging/event publishing.
 * This defines how product events are published.
 */
public interface ProductEventPublisher {
    
    /**
     * Publish product created event
     */
    void publishProductCreated(Product product);
    
    /**
     * Publish product updated event
     */
    void publishProductUpdated(Product product);
    
    /**
     * Publish product deleted event
     */
    void publishProductDeleted(Long productId);
}
