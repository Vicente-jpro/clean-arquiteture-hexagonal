package com.food.ordering.system.application.publisher;

import com.food.ordering.system.domain.events.DomainEvent;

public interface DomainEventPublisher <T extends DomainEvent>{

	void publisher(T domainEvent);
}
