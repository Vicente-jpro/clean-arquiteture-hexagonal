package com.food.ordering.system.application.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.food.ordering.system.application.dto.OrderAddress;
import com.food.ordering.system.application.dto.OrderItem;

import jakarta.validation.constraints.NotNull;

public record CreateOrderCommand (
		
		
		@NotNull UUID customerId,
		
		@NotNull UUID restaurantId,
		
		@NotNull BigDecimal price,
		
		@NotNull List<OrderItem> items,
		
		@NotNull OrderAddress address
		
		){

}
