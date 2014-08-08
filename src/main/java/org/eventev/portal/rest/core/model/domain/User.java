package org.eventev.portal.rest.core.model.domain;

import java.util.Map;

import org.eventev.portal.rest.core.model.domain.UserPasswordTypeEnum.Password;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id
	private String id;
	
	private String login;
	private String email;
	private String title;
	private String firstname;
	private String lastname;

	private Password password;

	// Be careful! Timezone influences change of age.
	private DateTime birthday;

	private Map<String, Object> extendedProfile;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public DateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(DateTime birthday) {
		this.birthday = birthday;
	}

	public Period getAge() {
		return new Period(birthday, new DateTime());
	}

	public Object getExtendedInfo(String key) {
		Object value = extendedProfile.get(key);
		return value;
	}

	public void setExtendedInfo(String key, Object value) {
		extendedProfile.put(key, value);
	}

}