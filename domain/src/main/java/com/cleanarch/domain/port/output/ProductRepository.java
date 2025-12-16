package com.cleanarch.domain.port.output;

import com.cleanarch.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Output Port (Repository Interface) - Hexagonal Architecture
 * This is the contract that infrastructure adapters must implement
 * Domain layer defines the interface, infrastructure layer implements it
 */
public interface ProductRepository {
    
    Product save(Product product);
    
    Optional<Product> findById(Long id);
    
    List<Product> findAll();
    
    List<Product> findByCategory(String category);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}
