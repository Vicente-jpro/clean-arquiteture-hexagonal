package com.cleanarch.application.port.input;

import com.cleanarch.domain.model.Product;

import java.util.List;

/**
 * Input Port (Use Case Interface) - Hexagonal Architecture
 * This defines the operations that can be performed on products
 * This is what the REST adapter will call
 */
public interface ProductUseCase {
    
    Product createProduct(Product product);
    
    Product updateProduct(Long id, Product product);
    
    Product getProductById(Long id);
    
    List<Product> getAllProducts();
    
    List<Product> getProductsByCategory(String category);
    
    void deleteProduct(Long id);
}
