package com.food.ordering.system.application.dto.message;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.food.ordering.system.domain.valueobject.PaymentStatus;

import jakarta.validation.constraints.NotNull;

public record PaymentResponse ( 
		
		@NotNull String id,
		
		@NotNull String sagaId,
		
		@NotNull String orderId,
		
		@NotNull String paymentId,
		
		@NotNull String customerId,
		
		@NotNull BigDecimal price,
		
		@NotNull Instant createAt,
		
		@NotNull PaymentStatus paymentStatus,
		
		@NotNull List<String> failureMessages
		
		) {

}
