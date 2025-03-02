package com.project.david.dto.employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	// Login -> 只需要 username 和 password
	
	private String username;
	private String password;

	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public LoginRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
