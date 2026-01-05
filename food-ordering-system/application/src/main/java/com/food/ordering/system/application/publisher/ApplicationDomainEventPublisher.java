package com.food.ordering.system.application.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.food.ordering.system.domain.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationDomainEventPublisher 
implements ApplicationEventPublisherAware, DomainEventPublisher<OrderCreatedEvent>{
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		
		this.applicationEventPublisher = applicationEventPublisher;
		
	}

	@Override
	public void publisher(OrderCreatedEvent domainEvent) {
		this.applicationEventPublisher.publishEvent(domainEvent);
		
		log.info("OrdercreatedEvent is published for order id: {}", domainEvent.getOrder().getId().getValue());
	}



	
	
	
}
