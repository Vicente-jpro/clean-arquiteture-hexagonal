package com.hexagonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity representing the core business model.
 * This is a domain entity with no infrastructure dependencies.
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
     * Business rule: validate if product is in stock
     */
    public boolean isInStock() {
        return quantity != null && quantity > 0;
    }
    
    /**
     * Business rule: validate if product is valid
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty()
                && price != null && price.compareTo(BigDecimal.ZERO) > 0
                && quantity != null && quantity >= 0;
    }
    
    /**
     * Business rule: reduce stock quantity
     */
    public void reduceStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (quantity < amount) {
            throw new IllegalStateException("Insufficient stock");
        }
        this.quantity -= amount;
    }
    
    /**
     * Business rule: increase stock quantity
     */
    public void increaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.quantity += amount;
    }
}
