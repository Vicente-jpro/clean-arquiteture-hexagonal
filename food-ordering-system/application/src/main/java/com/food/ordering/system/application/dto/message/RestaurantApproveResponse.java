package com.food.ordering.system.application.dto.message;

import java.util.List;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;

public record RestaurantApproveResponse(
		
		String id,
		
		String sagaId,
		
		String orderId,
		
		String restaurantId,
		
		OrderApprovalStatus orderApprovalStatus,
		
		List<String> failureMessages
		) {

}
