package com.food.ordering.system.domain.events;

import java.time.ZonedDateTime;

import com.food.ordering.system.domain.entities.Order;


public class OrderCreatedEvent extends OrderEvent {

	public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
		super(order, createdAt);
	}


}
