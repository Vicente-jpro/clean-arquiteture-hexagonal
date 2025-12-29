package com.food.ordering.system.application.ports;


import com.food.ordering.system.application.command.CreateOrderCommand;
import com.food.ordering.system.application.dto.CreateOrderResponse;
import com.food.ordering.system.application.dto.track.TrackOrderQuery;
import com.food.ordering.system.application.dto.track.TrackOrderResponse;

import jakarta.validation.Valid;

public interface OrderApplicationService {
	
	CreateOrderResponse execute(@Valid CreateOrderCommand command);
	
	TrackOrderResponse trackOrder(@Valid  TrackOrderQuery query);

}
