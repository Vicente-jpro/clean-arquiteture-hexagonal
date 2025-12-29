package com.food.ordering.system.domain.exceptions;

public abstract class DomainException extends RuntimeException{

	public DomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public DomainException(String message) {
		super(message);
	}
	
	

}
