package com.food.ordering.system.application.publisher;

import com.food.ordering.system.domain.events.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher 
extends DomainEventPublisher<OrderPaidEvent>{

}
