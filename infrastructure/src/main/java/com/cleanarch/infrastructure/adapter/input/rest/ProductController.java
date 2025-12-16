package com.cleanarch.infrastructure.adapter.input.rest;

import com.cleanarch.application.port.input.ProductUseCase;
import com.cleanarch.infrastructure.adapter.input.rest.dto.ProductRequest;
import com.cleanarch.infrastructure.adapter.input.rest.dto.ProductResponse;
import com.cleanarch.infrastructure.adapter.input.rest.mapper.ProductRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller (Input Adapter)
 * This is the primary adapter that drives the application
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductUseCase productUseCase;
    private final ProductRestMapper mapper;
    
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        var product = mapper.toDomain(request);
        var createdProduct = productUseCase.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdProduct));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        var product = productUseCase.getProductById(id);
        return ResponseEntity.ok(mapper.toResponse(product));
    }
    
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String category) {
        
        List<ProductResponse> products;
        if (category != null && !category.isEmpty()) {
            products = productUseCase.getProductsByCategory(category).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        } else {
            products = productUseCase.getAllProducts().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        }
        return ResponseEntity.ok(products);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody ProductRequest request) {
        var product = mapper.toDomain(request);
        var updatedProduct = productUseCase.updateProduct(id, product);
        return ResponseEntity.ok(mapper.toResponse(updatedProduct));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
