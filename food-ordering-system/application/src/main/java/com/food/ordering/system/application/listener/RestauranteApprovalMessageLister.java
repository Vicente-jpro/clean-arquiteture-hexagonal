package com.food.ordering.system.application.listener;

import com.food.ordering.system.application.dto.message.PaymentResponse;
import com.food.ordering.system.application.dto.message.RestaurantApproveResponse;

public interface RestauranteApprovalMessageLister {
	
	void orderApproved(RestaurantApproveResponse response);
	
	void orderRejected(RestaurantApproveResponse response);

}
