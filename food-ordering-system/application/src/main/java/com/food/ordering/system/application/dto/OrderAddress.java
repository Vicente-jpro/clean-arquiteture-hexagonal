package com.food.ordering.system.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record OrderAddress (
		
		@NotNull @Max(50) String street,
		
		@NotNull @Max(10) String postalCode,
		
		@NotNull @Max(50) String city
		
		) {

}
