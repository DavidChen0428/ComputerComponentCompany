package com.project.david.dto.employee;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeUpdateDTO {
	// 不需要 username 的 Employee
	
	private Integer id;
	private String name;
	private String password;
	private String position;
	private String department;

	public EmployeeUpdateDTO(Integer id, String name, String password, String position, String department) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.position = position;
		this.department = department;
	}

	public EmployeeUpdateDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
