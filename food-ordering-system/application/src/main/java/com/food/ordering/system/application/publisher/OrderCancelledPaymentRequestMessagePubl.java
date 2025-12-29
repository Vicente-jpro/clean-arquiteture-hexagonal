package com.food.ordering.system.application.publisher;

import com.food.ordering.system.domain.events.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePubl 
extends DomainEventPublisher<OrderCancelledEvent>{

}
