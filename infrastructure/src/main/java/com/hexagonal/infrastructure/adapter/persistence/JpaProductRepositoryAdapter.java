package com.hexagonal.infrastructure.adapter.persistence;

import com.hexagonal.domain.model.Product;
import com.hexagonal.domain.port.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of the ProductRepository port.
 * This adapter bridges the domain and the persistence layer.
 */
@Component
@RequiredArgsConstructor
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final SpringDataProductRepository jpaRepository;
    private final ProductMapper mapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Product> findByCategory(String category) {
        return jpaRepository.findByCategory(category).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
