package com.food.ordering.system.domain.entities;

import java.util.List;

import com.food.ordering.system.domain.common.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestaurantId;

import lombok.Getter;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId>{
	
	private List<Product> products;
	private boolean activate;
	
	 

}
