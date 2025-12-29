package com.food.ordering.system.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record OrderItem(
		
		
		@NotNull UUID productId,
		
		@NotNull Integer quantity,
		
		@NotNull BigDecimal price,
		
		@NotNull BigDecimal subtotal
		
		
		) {

}
