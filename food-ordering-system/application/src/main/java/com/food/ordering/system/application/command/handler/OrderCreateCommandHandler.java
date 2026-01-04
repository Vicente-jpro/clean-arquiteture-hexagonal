package com.food.ordering.system.application.command.handler;

import org.springframework.stereotype.Component;

import com.food.ordering.system.application.command.CreateOrderCommand;
import com.food.ordering.system.application.dto.CreateOrderResponse;
import com.food.ordering.system.application.helper.OrderCreateHelper;
import com.food.ordering.system.application.mapper.OrderDataMapper;
import com.food.ordering.system.domain.events.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {
	
	private final OrderCreateHelper orderCreateHelper;
	
	public final OrderDataMapper orderDataMapper;
	
	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
		
		OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
		
		log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
		
		return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
	}
	
}
