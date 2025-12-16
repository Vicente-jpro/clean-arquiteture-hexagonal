package com.hexagonal.infrastructure.adapter.rest;

import com.hexagonal.domain.model.Product;
import com.hexagonal.domain.port.in.ProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product operations.
 * This adapter exposes HTTP endpoints for product management.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductRestMapper mapper;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto request) {
        log.info("REST request to create product: {}", request.getName());
        Product product = mapper.toDomain(request);
        Product createdProduct = productUseCase.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request) {
        log.info("REST request to update product with id: {}", id);
        Product product = mapper.toDomain(request);
        Product updatedProduct = productUseCase.updateProduct(id, product);
        return ResponseEntity.ok(mapper.toResponse(updatedProduct));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        log.info("REST request to get product with id: {}", id);
        Product product = productUseCase.getProductById(id);
        return ResponseEntity.ok(mapper.toResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        log.info("REST request to get all products");
        List<Product> products = productUseCase.getAllProducts();
        List<ProductResponseDto> response = products.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product with id: {}", id);
        productUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(
            @PathVariable String category) {
        log.info("REST request to get products by category: {}", category);
        List<Product> products = productUseCase.getProductsByCategory(category);
        List<ProductResponseDto> response = products.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
