package com.project.david.dto.order;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderProductDTO {
	private Integer id;
	private String name;
	private double price;
	private int quantity;

	public OrderProductDTO(Integer id, String name, double price, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public OrderProductDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
