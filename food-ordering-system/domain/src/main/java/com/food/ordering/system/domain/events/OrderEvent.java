package com.food.ordering.system.domain.events;

import java.time.ZonedDateTime;

import com.food.ordering.system.domain.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {
	
	private Order order;
	private ZonedDateTime createdAt;

}
