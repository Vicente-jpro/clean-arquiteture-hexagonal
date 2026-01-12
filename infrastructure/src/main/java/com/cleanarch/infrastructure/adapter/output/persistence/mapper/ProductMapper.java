package com.cleanarch.infrastructure.adapter.output.persistence.mapper;

import com.cleanarch.domain.model.Product;
import com.cleanarch.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Domain Model and JPA Entity
 * This keeps the domain layer clean and independent of infrastructure
 */
@Component
public class ProductMapper {
    
    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductEntity.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .category(product.getCategory())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .build();
    }
    
    public Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Product.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .price(entity.getPrice())
            .quantity(entity.getQuantity())
            .category(entity.getCategory())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
