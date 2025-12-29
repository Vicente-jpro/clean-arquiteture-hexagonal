package com.food.ordering.system.domain.events;

import java.time.ZonedDateTime;

import com.food.ordering.system.domain.entities.Order;

public class OrderPaidEvent extends OrderEvent{

	public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
		super(order, createdAt);
	}


}
