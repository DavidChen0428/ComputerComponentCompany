package com.project.david.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
	private Integer id;
	private String name;
	private double price;
	private int quantity;
	private Integer orderId;

	public ProductDTO(Integer id, String name, double price, int quantity, Integer orderId) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.orderId = orderId;
	}

	public ProductDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
