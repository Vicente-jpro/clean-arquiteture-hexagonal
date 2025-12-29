package com.food.ordering.system.domain.events;

import java.time.ZonedDateTime;

import com.food.ordering.system.domain.entities.Order;


public class OrderCancelledEvent extends OrderEvent {

	public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
		super(order, createdAt);
	}


}
