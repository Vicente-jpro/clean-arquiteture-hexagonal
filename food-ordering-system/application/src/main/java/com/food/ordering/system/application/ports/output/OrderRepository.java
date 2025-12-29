package com.food.ordering.system.application.ports.output;

import java.util.Optional;

import com.food.ordering.system.domain.entities.Order;
import com.food.ordering.system.domain.valueobject.TrackingId;

public interface OrderRepository {
	
	Order save(Order order);
	
	Optional<Order> findByTrackingId(TrackingId trackingId);

}
