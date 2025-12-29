package com.food.ordering.system.domain.events.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.food.ordering.system.domain.entities.Order;
import com.food.ordering.system.domain.entities.Restaurant;
import com.food.ordering.system.domain.events.OrderCancelledEvent;
import com.food.ordering.system.domain.events.OrderCreatedEvent;
import com.food.ordering.system.domain.events.OrderDomainService;
import com.food.ordering.system.domain.events.OrderPaidEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{

	private static final String UTC = "UTC";
	@Override
	public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
		validateRestaurant(restaurant);
		setOrderProductInformation(order , restaurant);
	
		log.info("Order with id {} has been initiated", order.getId());
		return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
	}
	
	private void validateRestaurant(Restaurant restaurant) {
		if (!restaurant.isActivate()) {
			throw new IllegalStateException("Restaurant with id " + restaurant.getId() + " is not active!");
		}
	}


	private void setOrderProductInformation(Order order, Restaurant restaurant) {
		order.getItems().forEach(orderItem -> {
			boolean productExists = restaurant.getProducts()
				.stream()
				.anyMatch(product -> product.equals(orderItem.getProduct()));

			if (!productExists) {
				throw new IllegalStateException("Product with id " + orderItem.getProduct().getId() +
						" is not in restaurant " + restaurant.getId());	
			}

			orderItem.getProduct().updateWithConfirmedNameAndPrice(orderItem.getProduct());
			});
		
	}

	@Override
	public OrderPaidEvent payOrder(Order order) {
		order.pay();
		log.info("Order with id: {} is paid.", order.getId());
		return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
	}

	@Override
	public void approvedOrder(Order order) {
		order.approve();
		log.info("Order with id: {}", order.getId());
		
		
	}

	@Override
	public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
		
		order.initCancelation(failureMessages);
		log.info("Order payment is cancelling for order id: {}", order.getId());
		return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
	}

	@Override
	public void cancelOrder(Order order, List<String> failureMessages) {
		
		order.cancel(failureMessages);
		log.info("Order with id: {} cancelled", order.getId());
		
	}
	

}
