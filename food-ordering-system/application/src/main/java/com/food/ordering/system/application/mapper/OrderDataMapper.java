package com.food.ordering.system.application.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.food.ordering.system.application.command.CreateOrderCommand;
import com.food.ordering.system.application.dto.CreateOrderResponse;
import com.food.ordering.system.application.dto.OrderAddress;
import com.food.ordering.system.domain.entities.Order;
import com.food.ordering.system.domain.entities.OrderItem;
import com.food.ordering.system.domain.entities.Product;
import com.food.ordering.system.domain.entities.Restaurant;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.domain.valueobject.StreetAddress;

import jakarta.validation.constraints.NotNull;

@Component
public class OrderDataMapper {
	
	public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
		return Restaurant.builder()
				.restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
				.products(createOrderCommand.items()
						.stream()
						.map( product -> new Product(new ProductId(product.productId())) 
								).collect(Collectors.toList())
						)
				.build();
	}
	

	
	public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
		
		
		return Order.builder()
				.customerId( new CustomerId(createOrderCommand.customerId()))
				.restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
				.streetAddress( orderAddressToStreetAddress(createOrderCommand.address()) )
				.price(new Money(createOrderCommand.price()))
				.items(orderItemsToOrderEntities(createOrderCommand.items()))
				.build();
		
		
	}



	private List<OrderItem> orderItemsToOrderEntities(
			@NotNull List<com.food.ordering.system.application.dto.OrderItem> orderItems) {

		
		return orderItems.stream()
				.map(orderItem -> OrderItem.builder()
						.product(new Product( new ProductId(orderItem.productId())))
						.price(new Money(orderItem.price()))
						.quantity(orderItem.quantity())
						.subtotal( new Money(orderItem.subtotal()))
						.build()
				
						).collect(Collectors.toList());
	}



	private StreetAddress orderAddressToStreetAddress(@NotNull OrderAddress address) {

		return new StreetAddress(
				UUID.randomUUID(), 
				address.postalCode(), 
				address.street());
	}
	
	public CreateOrderResponse orderToCreateOrderResponse(Order order) {
		
		return new CreateOrderResponse(
				order.getTrackingId().getValue(), 
				order.getStatus(), 
				"");
	}
}
