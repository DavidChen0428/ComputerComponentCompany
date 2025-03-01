package com.project.david.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEmployeeDTO {
	private Integer id;
	private String name;

	public OrderEmployeeDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public OrderEmployeeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
