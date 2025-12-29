package com.food.ordering.system.domain.events;

import java.util.List;

import com.food.ordering.system.domain.entities.Order;
import com.food.ordering.system.domain.entities.Restaurant;

public interface OrderDomainService {
	
	OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);
	
	OrderPaidEvent payOrder(Order order);
	
	void approvedOrder(Order order);
	
	public void cancelOrder(Order order, List<String> failureMessages);
	
	OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
}
