package org.eventev.portal.rest.utils.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class Money {

	private BigDecimal value;
	private Currency currency;
	
	public Money(BigDecimal value, Currency currency) {
		this.value = value.setScale(currency.getDefaultFractionDigits(), RoundingMode.UP);
		this.currency = currency;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
}
