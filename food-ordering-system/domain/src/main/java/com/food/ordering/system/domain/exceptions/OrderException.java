package com.food.ordering.system.domain.exceptions;

public class OrderException extends DomainException{

	public OrderException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderException(String message) {
		super(message);
	} 

}
