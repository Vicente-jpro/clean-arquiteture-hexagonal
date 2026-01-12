package com.cleanarch.infrastructure.adapter.output.persistence.repository;

import com.cleanarch.domain.model.Product;
import com.cleanarch.domain.port.output.ProductRepository;
import com.cleanarch.infrastructure.adapter.output.persistence.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA Adapter - Implements the ProductRepository port
 * This is the adapter that connects the domain to the database
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {
    
    private final JpaProductRepository jpaProductRepository;
    private final ProductMapper productMapper;
    
    @Override
    public Product save(Product product) {
        var entity = productMapper.toEntity(product);
        var savedEntity = jpaProductRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        return jpaProductRepository.findById(id)
            .map(productMapper::toDomain);
    }
    
    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll().stream()
            .map(productMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByCategory(String category) {
        return jpaProductRepository.findByCategory(category).stream()
            .map(productMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaProductRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaProductRepository.existsById(id);
    }
}
