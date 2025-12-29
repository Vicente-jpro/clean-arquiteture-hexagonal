package com.food.ordering.system.application.dto.track;

import java.util.List;
import java.util.UUID;

import com.food.ordering.system.domain.valueobject.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record TrackOrderResponse(
		
		@NotNull UUID orderTrackingId,
		
		@NotNull OrderStatus orderStatus,
		
		List<String> failureMessage
		
		) {

}
