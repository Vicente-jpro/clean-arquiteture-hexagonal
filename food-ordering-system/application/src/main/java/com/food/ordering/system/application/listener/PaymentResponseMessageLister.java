package com.food.ordering.system.application.listener;

import com.food.ordering.system.application.dto.message.PaymentResponse;

public interface PaymentResponseMessageLister {
	
	void paymentCompleted(PaymentResponse response);
	
	void paymentCancelled(PaymentResponse response);

}
