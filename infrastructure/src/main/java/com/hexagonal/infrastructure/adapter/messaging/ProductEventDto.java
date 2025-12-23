package com.hexagonal.infrastructure.adapter.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Product events sent to Kafka.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEventDto {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String eventType; // CREATED, UPDATED, DELETED
    private LocalDateTime eventTimestamp;
}
