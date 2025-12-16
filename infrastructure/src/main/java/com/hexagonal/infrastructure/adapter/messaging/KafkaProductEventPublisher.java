package com.hexagonal.infrastructure.adapter.messaging;

import com.hexagonal.domain.model.Product;
import com.hexagonal.domain.port.out.ProductEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Kafka implementation of ProductEventPublisher.
 * This adapter publishes product events to Kafka topics.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProductEventPublisher implements ProductEventPublisher {

    private final KafkaTemplate<String, ProductEventDto> kafkaTemplate;
    private static final String TOPIC = "product-events";

    @Override
    public void publishProductCreated(Product product) {
        log.info("Publishing product created event for product id: {}", product.getId());
        ProductEventDto event = buildEventDto(product, "CREATED");
        kafkaTemplate.send(TOPIC, product.getId().toString(), event);
    }

    @Override
    public void publishProductUpdated(Product product) {
        log.info("Publishing product updated event for product id: {}", product.getId());
        ProductEventDto event = buildEventDto(product, "UPDATED");
        kafkaTemplate.send(TOPIC, product.getId().toString(), event);
    }

    @Override
    public void publishProductDeleted(Long productId) {
        log.info("Publishing product deleted event for product id: {}", productId);
        ProductEventDto event = ProductEventDto.builder()
                .id(productId)
                .eventType("DELETED")
                .eventTimestamp(LocalDateTime.now())
                .build();
        kafkaTemplate.send(TOPIC, productId.toString(), event);
    }

    private ProductEventDto buildEventDto(Product product, String eventType) {
        return ProductEventDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .eventType(eventType)
                .eventTimestamp(LocalDateTime.now())
                .build();
    }
}
