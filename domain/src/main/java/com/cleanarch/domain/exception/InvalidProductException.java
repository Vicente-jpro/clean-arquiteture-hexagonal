package com.cleanarch.domain.exception;

/**
 * Domain exception for invalid product data
 */
public class InvalidProductException extends RuntimeException {
    
    public InvalidProductException(String message) {
        super(message);
    }
}
