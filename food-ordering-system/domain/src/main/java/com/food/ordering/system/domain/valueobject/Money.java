package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Money {
	
	private final BigDecimal amount;

	
	public boolean isGreaterThanZero() {
		return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
	}

	public boolean isGreaterThan(Money money) {
		return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
	}
	
	public Money add(Money money) {
		final var moneyScaled = setScale(money.getAmount());
		return new Money(this.amount.add(moneyScaled));
	}
	
	public Money subtract(Money money) {
		final var moneyScaled = setScale(money.getAmount());
		return new Money(this.amount.subtract(moneyScaled));
	}
	

	public BigDecimal setScale(BigDecimal input) {
		// with scale 2, the number of digits after decimal point is 2, e.g 10.75 or 500.80
		return input.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		return Objects.equals(amount, other.amount);
	}


}
