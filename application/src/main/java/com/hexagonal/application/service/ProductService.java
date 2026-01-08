package com.hexagonal.application.service;

import com.hexagonal.domain.exception.InvalidProductException;
import com.hexagonal.domain.exception.ProductNotFoundException;
import com.hexagonal.domain.model.Product;
import com.hexagonal.domain.port.in.ProductUseCase;
import com.hexagonal.domain.port.out.ProductEventPublisher;
import com.hexagonal.domain.port.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of ProductUseCase.
 * This service orchestrates the business logic and coordinates between ports.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final ProductEventPublisher productEventPublisher;

    @Override
    public Product createProduct(Product product) {
        log.info("Creating product: {}", product.getName());
        
        // Validate product
        if (!product.isValid()) {
            throw new InvalidProductException("Product validation failed");
        }
        
        // Set timestamps
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        // Save product
        Product savedProduct = productRepository.save(product);
        
        // Publish event
        productEventPublisher.publishProductCreated(savedProduct);
        
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return savedProduct;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        log.info("Updating product with id: {}", id);
        
        // Check if product exists
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        // Validate updated product
        if (!product.isValid()) {
            throw new InvalidProductException("Product validation failed");
        }
        
        // Update fields
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        // Save updated product
        Product updatedProduct = productRepository.save(existingProduct);
        
        // Publish event
        productEventPublisher.publishProductUpdated(updatedProduct);
        
        log.info("Product updated successfully with id: {}", updatedProduct.getId());
        return updatedProduct;
    }

    @Override
    public Product getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        
        // Check if product exists
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        
        // Delete product
        productRepository.deleteById(id);
        
        // Publish event
        productEventPublisher.publishProductDeleted(id);
        
        log.info("Product deleted successfully with id: {}", id);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategory(category);
    }
}
