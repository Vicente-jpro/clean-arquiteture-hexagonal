package com.hexagonal.domain.port.in;

import com.hexagonal.domain.model.Product;

import java.util.List;

/**
 * Input port (use case interface) for product operations.
 * This defines the operations that can be performed on products.
 */
public interface ProductUseCase {
    
    /**
     * Create a new product
     */
    Product createProduct(Product product);
    
    /**
     * Update an existing product
     */
    Product updateProduct(Long id, Product product);
    
    /**
     * Get a product by its id
     */
    Product getProductById(Long id);
    
    /**
     * Get all products
     */
    List<Product> getAllProducts();
    
    /**
     * Delete a product by its id
     */
    void deleteProduct(Long id);
    
    /**
     * Search products by category
     */
    List<Product> getProductsByCategory(String category);
}
