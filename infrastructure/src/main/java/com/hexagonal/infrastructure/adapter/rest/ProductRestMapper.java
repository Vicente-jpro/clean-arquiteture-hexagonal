package com.hexagonal.infrastructure.adapter.rest;

import com.hexagonal.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper to convert between REST DTOs and domain Product.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductRestMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toDomain(ProductRequestDto dto);
    
    @Mapping(target = "inStock", expression = "java(product.isInStock())")
    ProductResponseDto toResponse(Product product);
}
