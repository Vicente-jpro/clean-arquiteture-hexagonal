package com.cleanarch.infrastructure.adapter.output.messaging;

import com.cleanarch.domain.model.Product;
import com.cleanarch.domain.port.output.ProductEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka Adapter - Implements the ProductEventPublisher port
 * This adapter publishes events to Kafka
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProductEventPublisher implements ProductEventPublisher {
    
    private static final String TOPIC = "product-events";
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    @Override
    public void publishProductCreated(Product product) {
        publishEvent("PRODUCT_CREATED", product);
    }
    
    @Override
    public void publishProductUpdated(Product product) {
        publishEvent("PRODUCT_UPDATED", product);
    }
    
    @Override
    public void publishProductDeleted(Long productId) {
        publishEvent("PRODUCT_DELETED", productId);
    }
    
    private void publishEvent(String eventType, Object payload) {
        try {
            var event = new ProductEvent(eventType, payload);
            var message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, message);
            log.info("Published event: {}", eventType);
        } catch (JsonProcessingException e) {
            log.error("Error publishing event: {}", eventType, e);
            throw new RuntimeException("Failed to publish event: " + eventType, e);
        }
    }
    
    record ProductEvent(String eventType, Object payload) {}
}
