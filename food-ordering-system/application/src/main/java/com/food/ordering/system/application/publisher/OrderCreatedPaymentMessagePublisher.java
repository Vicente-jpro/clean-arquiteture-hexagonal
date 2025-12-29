package com.food.ordering.system.application.publisher;

import com.food.ordering.system.domain.events.OrderCreatedEvent;

public interface OrderCreatedPaymentMessagePublisher 
extends DomainEventPublisher<OrderCreatedEvent> {
	
	

}
