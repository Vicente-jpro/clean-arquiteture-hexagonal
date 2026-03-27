package com.food.ordering.system.infrastructure.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.ordering.system.application.ports.OrderApplicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

	private final OrderApplicationService orderApplicationService;

}
