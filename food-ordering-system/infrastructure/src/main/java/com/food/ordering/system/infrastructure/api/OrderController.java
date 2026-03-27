package com.food.ordering.system.infrastructure.api;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

	private final OrderApplicationService orderApplicationService;
	
	public OrderController(OrderApplicationService orderApplicationService) {
		this.orderApplicationService = orderApplicationService;
	}
}
