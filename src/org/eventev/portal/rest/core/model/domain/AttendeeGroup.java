package org.eventev.portal.rest.core.model.domain;

import org.eventev.portal.rest.utils.data.Money;
import org.eventev.portal.rest.utils.data.TimePeriod;

public class AttendeeGroup {

	private String name;
	private Money price;
	private TimePeriod registrationPeriod;
	
	public AttendeeGroup(String name, Money price, TimePeriod registrationPeriod) {
		this.name = name;
		this.price = price;
		this.registrationPeriod = registrationPeriod;
	}
	
	public String getName() {
		return name;
	}
	
	public Money getPrice() {
		return price;
	}
	
	public TimePeriod getRegistrationPeriod() {
		return registrationPeriod;
	}
	
}
