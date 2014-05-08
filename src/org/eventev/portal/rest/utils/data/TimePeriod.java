package org.eventev.portal.rest.utils.data;

import org.joda.time.DateTime;

// TODO: Refactor to better name
public class TimePeriod {
	
	private DateTime start, end;
	
	public TimePeriod(DateTime start, DateTime end) {
		this.start = start;
		this.end = end;
	}
	
	public DateTime getStart() {
		return start;
	}
	
	public DateTime getEnd() {
		return end;
	}
	
}
