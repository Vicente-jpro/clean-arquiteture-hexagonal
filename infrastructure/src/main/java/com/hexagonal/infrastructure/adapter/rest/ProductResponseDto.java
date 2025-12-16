package com.hexagonal.infrastructure.adapter.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Product responses from REST API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean inStock;
}
