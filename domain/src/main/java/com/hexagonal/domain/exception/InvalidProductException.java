package com.hexagonal.domain.exception;

/**
 * Exception thrown when a product validation fails.
 */
public class InvalidProductException extends RuntimeException {
    
    public InvalidProductException(String message) {
        super(message);
    }
}
