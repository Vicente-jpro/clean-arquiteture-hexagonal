package com.food.ordering.system.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StreetAddress {
	
	private UUID id;
	private String postalCode;
	private String street;
	
	
	@Override
	public int hashCode() {
		return Objects.hash(id, postalCode, street);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StreetAddress other = (StreetAddress) obj;
		return Objects.equals(id, other.id) && Objects.equals(postalCode, other.postalCode)
				&& Objects.equals(street, other.street);
	}
	
	

}
