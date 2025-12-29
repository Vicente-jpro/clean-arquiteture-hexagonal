package com.food.ordering.system.domain.entities;

import java.util.UUID;

import com.food.ordering.system.domain.common.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderItemId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.domain.valueobject.StreetAddress;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class OrderItem extends AggregateRoot<OrderItemId>{
	
	private OrderItemId id;
	private OrderId orderId;
	private  Product product;
	private  int quantity;
	private  Money price;
	private  Money subtotal;
	
	private CustomerId customerId;
	private RestaurantId restaurantId;
	private StreetAddress streetAddress;
	
	public static OrderItem create(@NonNull final Order order) {

		final var orderItem = new OrderItem();
		orderItem.id = new OrderItemId(UUID.randomUUID());
		orderItem.orderId = order.getId();
		
		return orderItem;
		
	}
		


}
