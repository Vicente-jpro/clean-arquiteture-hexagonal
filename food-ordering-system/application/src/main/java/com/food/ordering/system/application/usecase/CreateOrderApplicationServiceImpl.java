package com.food.ordering.system.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.food.ordering.system.application.command.CreateOrderCommand;
import com.food.ordering.system.application.command.handler.OrderCreateCommandHandler;
import com.food.ordering.system.application.command.handler.OrderTrackCommandHandler;
import com.food.ordering.system.application.dto.CreateOrderResponse;
import com.food.ordering.system.application.dto.track.TrackOrderQuery;
import com.food.ordering.system.application.dto.track.TrackOrderResponse;
import com.food.ordering.system.application.ports.OrderApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class CreateOrderApplicationServiceImpl implements OrderApplicationService {

	private final OrderCreateCommandHandler orderCommandHandler;
	
	private final OrderTrackCommandHandler trackCommandHandler;
	
	@Override
	public CreateOrderResponse execute(@Valid CreateOrderCommand command) {
		return orderCommandHandler.createOrder(command);
	}

	@Override
	public TrackOrderResponse trackOrder(@Valid TrackOrderQuery query) {

		return trackCommandHandler.trackOrder(query);
	}
	
	
	
	
	

}
