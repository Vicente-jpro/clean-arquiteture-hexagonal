package com.food.ordering.system.application.command.handler;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.food.ordering.system.application.command.CreateOrderCommand;
import com.food.ordering.system.application.dto.CreateOrderResponse;
import com.food.ordering.system.application.helper.OrderCreateHelper;
import com.food.ordering.system.application.mapper.OrderDataMapper;
import com.food.ordering.system.application.ports.output.CustomerRepository;
import com.food.ordering.system.application.ports.output.OrderRepository;
import com.food.ordering.system.application.ports.output.RestaurantRepository;
import com.food.ordering.system.application.publisher.ApplicationDomainEventPublisher;
import com.food.ordering.system.domain.entities.Customer;
import com.food.ordering.system.domain.entities.Order;
import com.food.ordering.system.domain.entities.Restaurant;
import com.food.ordering.system.domain.events.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {
	
	private final OrderCreateHelper orderCreateHelper;
	
	public final OrderDataMapper orderDataMapper;
	
	private final OrderDataMapper orderDataMapper;
	
	private final ApplicationDomainEventPublisher applicationDomainEventPublisher; 
	
	@Transactional
	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
		checkCustomer(createOrderCommand.customerId()); 
		
		Restaurant restaurant = checkRestaurant(createOrderCommand);
		
		Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
		
		OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
		Order orderResult = saveOrder(order);
		
		log.info("Order is created with id: {}", orderResult.getId().getValue());
		
		applicationDomainEventPublisher.publisher(orderCreatedEvent);
		
		return orderDataMapper.orderToCreateOrderResponse(orderResult);
	}
	
	private void checkCustomer(UUID customerId) {
		
		OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
		
		log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
		
		return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
	}
	
}
