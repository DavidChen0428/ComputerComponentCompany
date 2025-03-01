package com.project.david.dto.employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRegisterDTO {
	// Register -> 不需要 id 的 Employee
	private String name;
	private String username;
	private String password;
	private String position;
	private String department;
	
	public EmployeeRegisterDTO(String name, String username, String password, String position, String department) {
		super();
		this.name = name;
		this.username = username;
		this.password = password;
		this.position = position;
		this.department = department;
	}

	public EmployeeRegisterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
