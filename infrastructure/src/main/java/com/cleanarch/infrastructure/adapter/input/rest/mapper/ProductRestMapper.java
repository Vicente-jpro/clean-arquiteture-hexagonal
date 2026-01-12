package com.cleanarch.infrastructure.adapter.input.rest.mapper;

import com.cleanarch.domain.model.Product;
import com.cleanarch.infrastructure.adapter.input.rest.dto.ProductRequest;
import com.cleanarch.infrastructure.adapter.input.rest.dto.ProductResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper between REST DTOs and Domain Models
 */
@Component
public class ProductRestMapper {
    
    public Product toDomain(ProductRequest request) {
        if (request == null) {
            return null;
        }
        
        return Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .quantity(request.getQuantity())
            .category(request.getCategory())
            .build();
    }
    
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductResponse.builder()
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
}
