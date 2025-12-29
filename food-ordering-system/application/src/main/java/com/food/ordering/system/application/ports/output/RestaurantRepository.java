package com.food.ordering.system.application.ports.output;

import java.util.Optional;

import com.food.ordering.system.domain.entities.Restaurant;

public interface RestaurantRepository {

	Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
