package com.hexagonal.domain.port.out;

import com.hexagonal.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Output port (repository interface) for product persistence.
 * This defines how products are persisted, without specifying implementation details.
 */
public interface ProductRepository {
    
    /**
     * Save a product
     */
    Product save(Product product);
    
    /**
     * Find a product by its id
     */
    Optional<Product> findById(Long id);
    
    /**
     * Find all products
     */
    List<Product> findAll();
    
    /**
     * Delete a product by its id
     */
    void deleteById(Long id);
    
    /**
     * Check if a product exists by its id
     */
    boolean existsById(Long id);
    
    /**
     * Find products by category
     */
    List<Product> findByCategory(String category);
}
