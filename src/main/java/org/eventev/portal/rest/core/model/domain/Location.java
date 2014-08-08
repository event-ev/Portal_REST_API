package org.eventev.portal.rest.core.model.domain;

public class Location {

	private double lat, lng;
	private String description;
	
	public Location(double latitude, double longitude) {
		this.lat = latitude;
		this.lng = longitude;
	}
	
	public Location(double latitude, double longitude, String description) {
		this(latitude, longitude);
		this.description = description;
	}
	
	public double getLatitude() {
		return lat;
	}
	
	public double getLongitude() {
		return lng;
	}
	
	public String getDescription() {
		return description;
	}
	
}
