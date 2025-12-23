package com.hexagonal.infrastructure.adapter.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer to listen to product events.
 * This demonstrates event consumption in the hexagonal architecture.
 */
@Component
@Slf4j
public class ProductEventConsumer {

    @KafkaListener(topics = "product-events", groupId = "product-service-group")
    public void consumeProductEvent(ProductEventDto event) {
        log.info("Consumed product event: {} for product id: {}", 
                event.getEventType(), event.getId());
        
        // Here you can implement any business logic based on the event
        // For example: updating cache, triggering notifications, etc.
        switch (event.getEventType()) {
            case "CREATED":
                log.info("Product created: {}", event.getName());
                break;
            case "UPDATED":
                log.info("Product updated: {}", event.getName());
                break;
            case "DELETED":
                log.info("Product deleted with id: {}", event.getId());
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}
