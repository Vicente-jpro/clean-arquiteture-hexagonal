package com.food.ordering.system.application.dto;

import java.util.UUID;

import com.food.ordering.system.domain.valueobject.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record CreateOrderResponse(
		
		@NotNull UUID orderTrackingId, 
		
		@NotNull OrderStatus orderStatus,  
		
		@NotNull String message
		
		) {

}
