package org.eventev.portal.rest.utils.data;

import org.eventev.portal.rest.core.model.domain.Location;
import org.joda.time.DateTime;

public class EventDate {

	private DateTime start;
	private DateTime end;
	private String description;
	private Location location;
	
	public DateTime getStart() {
		return start;
	}
	public void setStart(DateTime start) {
		this.start = start;
	}
	public DateTime getEnd() {
		return end;
	}
	public void setEnd(DateTime end) {
		this.end = end;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
