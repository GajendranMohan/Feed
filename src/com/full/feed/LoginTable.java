package com.full.feed;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class LoginTable {

	@Persistent
	public String name;

	public String getUname() {
		return name;
	}

	public void setUname(String name) {
		this.name = name;
	}
	
	
	
	@Persistent
	public String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}