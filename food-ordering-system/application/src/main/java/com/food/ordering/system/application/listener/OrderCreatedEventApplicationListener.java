package com.food.ordering.system.application.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.food.ordering.system.application.publisher.OrderCreatedPaymentMessagePublisher;
import com.food.ordering.system.domain.events.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventApplicationListener {

	private final OrderCreatedPaymentMessagePublisher orderCreatedPaymentMessagePublisher;
	
	
	@TransactionalEventListener
	public void process(OrderCreatedEvent orderCreatedEvent) {
		
	}
	
}
