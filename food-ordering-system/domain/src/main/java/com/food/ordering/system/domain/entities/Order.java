package com.food.ordering.system.domain.entities;

import java.util.List;
import java.util.UUID;

import com.food.ordering.system.domain.common.AggregateRoot;
import com.food.ordering.system.domain.exceptions.OrderException;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.domain.valueobject.StreetAddress;
import com.food.ordering.system.domain.valueobject.TrackingId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Order extends AggregateRoot<OrderId>{
	
	private  OrderId orderId;
	private  CustomerId customerId;
	private  RestaurantId restaurantId;
	private  StreetAddress streetAddress;
	private  Money price;
	private  List<OrderItem> items;
	
	private TrackingId trackingId;
	private OrderStatus status;
	private List<String> failureMessages;
	
	public static Order create(){

		final var order = new Order();
		order.orderId = new OrderId(UUID.randomUUID());
		order.trackingId = new TrackingId(UUID.randomUUID());
		order.status = OrderStatus.PENDING;
		order.items.add( initializeOrderItems(order) );
		validateTotalPrice(order.price);
		return order;
	}
	
	
	static void validateTotalPrice(Money price) {
		if (price != null || !price.isGreaterThanZero() ) {
			throw new OrderException("Total price must be greater than 0.");
		}
	}
	
	public void pay() {
		if (!status.equals(OrderStatus.PENDING)) {
			throw new OrderException("Order is not correct state for pay operation.");
		}
		
		this.status = OrderStatus.PAID;
		
	}
	
	
	public void approve() {
		if (!status.equals(OrderStatus.PAID)) {
			throw new OrderException("Can not approve because order is not paid.");
		}
		
		status = OrderStatus.APPROVED;
	}
	
	public void cancel(List<String> failureMessages) {
		if (!status.equals(OrderStatus.PENDING) || !status.equals(OrderStatus.CANCELLING)) {
			updateFailueMessages(failureMessages);
			throw new OrderException("Can not cancel because order is not pending or in cancelling status.");
		}

		status = OrderStatus.CANCELLED;
		
	}
	
	public void initCancelation(List<String> failureMessages) {
		if (status.equals(OrderStatus.PAID)) {
			this.status = OrderStatus.CANCELLING;
			return;
		}
		
		updateFailueMessages(failureMessages);
		
		throw new OrderException("Can not initiate the cancelation because order is not status is not paid.");
	}
	
	private void updateFailueMessages(List<String> messages) {
		if (this.failureMessages != null && messages != null) {
			this.failureMessages.addAll(
					messages.stream()
					.filter(message -> !message.isEmpty()).toList());
		}
		
		
		if (this.failureMessages == null) {
			this.failureMessages = failureMessages;
		}
	}

	private static OrderItem initializeOrderItems(@NonNull final Order order) {
		return OrderItem.create(order);
	}
}
