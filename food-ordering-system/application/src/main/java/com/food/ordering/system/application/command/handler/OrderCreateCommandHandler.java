package com.food.ordering.system.application.command.handler;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.ordering.system.application.command.CreateOrderCommand;
import com.food.ordering.system.application.dto.CreateOrderResponse;
import com.food.ordering.system.application.mapper.OrderDataMapper;
import com.food.ordering.system.application.ports.output.CustomerRepository;
import com.food.ordering.system.application.ports.output.OrderRepository;
import com.food.ordering.system.application.ports.output.RestaurantRepository;
import com.food.ordering.system.domain.entities.Customer;
import com.food.ordering.system.domain.entities.Order;
import com.food.ordering.system.domain.entities.Restaurant;
import com.food.ordering.system.domain.events.OrderCreatedEvent;
import com.food.ordering.system.domain.events.OrderDomainService;
import com.food.ordering.system.domain.exceptions.OrderException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {
	
	private final OrderDomainService orderDomainService;
	
	private final OrderRepository orderRepository;
	
	private final CustomerRepository customerRepository;
	
	private final RestaurantRepository restauranteRepository;
	
	private final OrderDataMapper orderDataMapper;
	
	
	@Transactional
	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
		checkCustomer(createOrderCommand.customerId()); 
		
		Restaurant restaurant = checkRestaurant(createOrderCommand);
		
		Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
		
		OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
		Order orderResult = saveOrder(order);
		
		log.info("Order is created with id: {}", orderResult.getId().getValue());
		
		return orderDataMapper.orderToCreateOrderResponse(orderResult);
	}
	
	private void checkCustomer(UUID customerId) {
		
	Optional<Customer> customer = customerRepository.findCustomer(customerId);
		
		
		if (customer.isEmpty()) {
			log.warn("Could not find customer with customer id: {}", customerId);
			throw new OrderException("Could not find customer with customer id: " +customerId);
		}
	}
	
	private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
		
		Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
		
		Optional<Restaurant> restaurantFound = restauranteRepository.findRestaurantInformation(restaurant);

		if (restaurantFound.isEmpty()) {
			log.warn("Could not find restaurant with id: {}", createOrderCommand.restaurantId());
			throw new OrderException("Could not find restaurant with id: " +createOrderCommand.restaurantId());
		}
		
		return restaurantFound.get();
	}
	
	
	private Order saveOrder(Order order) {
		Order orderResult = orderRepository.save(order);
		
		if (orderResult == null) {
			throw new OrderException("Could not save order.");
		}
		
		log.info("Order is saved with id: {}", order.getId());
		
		return orderResult;
	}
}
