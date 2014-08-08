package org.eventev.portal.rest.core.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Event {

	@Id
	private String id;
	
	private String name;
	private String description;
	private EventVisibility visibility;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public EventVisibility getVisibility() {
		return visibility;
	}
	public void setVisibility(EventVisibility visibility) {
		this.visibility = visibility;
	}
	
	
	
}
