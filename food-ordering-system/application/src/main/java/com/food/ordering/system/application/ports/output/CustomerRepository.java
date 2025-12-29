package com.food.ordering.system.application.ports.output;

import java.util.Optional;
import java.util.UUID;

import com.food.ordering.system.domain.entities.Customer;

public interface CustomerRepository {
	
	Optional<Customer> findCustomer(UUID customerId);

}
