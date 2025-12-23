package com.cleanarch.infrastructure.adapter.output.persistence.repository;

import com.cleanarch.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository
 */
@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    
    List<ProductEntity> findByCategory(String category);
}
