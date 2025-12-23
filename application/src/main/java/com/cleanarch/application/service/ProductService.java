package com.cleanarch.application.service;

import com.cleanarch.application.port.input.ProductUseCase;
import com.cleanarch.domain.exception.InvalidProductException;
import com.cleanarch.domain.exception.ProductNotFoundException;
import com.cleanarch.domain.model.Product;
import com.cleanarch.domain.port.output.ProductEventPublisher;
import com.cleanarch.domain.port.output.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Product Service - Implements use cases
 * This is the application layer that orchestrates business logic
 */
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {
    
    private final ProductRepository productRepository;
    private final ProductEventPublisher eventPublisher;
    
    @Override
    public Product createProduct(Product product) {
        if (!product.isValid()) {
            StringBuilder errors = new StringBuilder("Product validation failed: ");
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                errors.append("name is required; ");
            }
            if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                errors.append("price must be non-negative; ");
            }
            if (product.getQuantity() == null || product.getQuantity() < 0) {
                errors.append("quantity must be non-negative; ");
            }
            throw new InvalidProductException(errors.toString());
        }
        
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        eventPublisher.publishProductCreated(savedProduct);
        
        return savedProduct;
    }
    
    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
        
        if (!product.isValid()) {
            StringBuilder errors = new StringBuilder("Product validation failed: ");
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                errors.append("name is required; ");
            }
            if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                errors.append("price must be non-negative; ");
            }
            if (product.getQuantity() == null || product.getQuantity() < 0) {
                errors.append("quantity must be non-negative; ");
            }
            throw new InvalidProductException(errors.toString());
        }
        
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        Product updatedProduct = productRepository.save(existingProduct);
        eventPublisher.publishProductUpdated(updatedProduct);
        
        return updatedProduct;
    }
    
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        
        productRepository.deleteById(id);
        eventPublisher.publishProductDeleted(id);
    }
}
