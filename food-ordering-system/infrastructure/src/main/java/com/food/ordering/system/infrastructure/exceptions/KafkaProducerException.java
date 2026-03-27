package com.food.ordering.system.infrastructure.exceptions;

public class KafkaProducerException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public KafkaProducerException(String message) {
		super(message);
	}

	
}
