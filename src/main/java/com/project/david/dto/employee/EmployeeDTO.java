package com.project.david.dto.employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {

	private Integer id;
	private String name;
	private String username;
	private String password; // 註冊或更新的時候使用
	private String position;
	private String department;

	public EmployeeDTO(Integer id, String name, String password, String position, String department) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.position = position;
		this.department = department;
	}

	public EmployeeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
