package com.food.ordering.system.application.dto.track;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TrackOrderQuery (
		
		@NotNull UUID orderTrackingId
		
		) {

}
