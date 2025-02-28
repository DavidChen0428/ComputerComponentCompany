package com.project.david.dto.employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDeleteDTO {
	private String username;
	private String password;
	
	public EmployeeDeleteDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public EmployeeDeleteDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
