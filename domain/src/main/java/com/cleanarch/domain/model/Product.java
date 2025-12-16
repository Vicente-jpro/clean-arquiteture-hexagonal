package com.cleanarch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity - Core domain model
 * This is the heart of our domain, independent of any framework or infrastructure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Business validation method
     */
    public boolean isAvailable() {
        return quantity != null && quantity > 0;
    }
    
    /**
     * Business validation method
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() 
            && price != null && price.compareTo(BigDecimal.ZERO) >= 0
            && quantity != null && quantity >= 0;
    }
}
