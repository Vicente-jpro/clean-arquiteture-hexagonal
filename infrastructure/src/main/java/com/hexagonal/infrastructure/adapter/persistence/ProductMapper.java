package com.hexagonal.infrastructure.adapter.persistence;

import com.hexagonal.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper to convert between domain Product and persistence ProductEntity.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    
    Product toDomain(ProductEntity entity);
    
    ProductEntity toEntity(Product domain);
}
