package com.food.ordering.system.application.command.handler;

import org.springframework.stereotype.Component;

import com.food.ordering.system.application.dto.track.TrackOrderQuery;
import com.food.ordering.system.application.dto.track.TrackOrderResponse;
import com.food.ordering.system.application.mapper.OrderDataMapper;
import com.food.ordering.system.application.ports.output.OrderRepository;
import com.food.ordering.system.domain.exceptions.OrderException;
import com.food.ordering.system.domain.valueobject.TrackingId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTrackCommandHandler {
	
	private final OrderRepository orderRepository;
	private final OrderDataMapper orderDataMapper;
	
	public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
		log.info("Tracking order with tracking id: {}", trackOrderQuery.orderTrackingId());
		
		var trackingId = new TrackingId(trackOrderQuery.orderTrackingId());
		
		var order = orderRepository.findByTrackingId(trackingId)
				.orElseThrow(() -> new OrderException(
						"Order with tracking id " + trackOrderQuery.orderTrackingId() + " not found"));
		
		return orderDataMapper.orderToTrackOrderResponse(order);
	}
	


}
