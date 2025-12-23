package com.hexagonal.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for ProductEntity.
 */
@Repository
public interface SpringDataProductRepository extends JpaRepository<ProductEntity, Long> {
    
    /**
     * Find products by category
     */
    List<ProductEntity> findByCategory(String category);
}
