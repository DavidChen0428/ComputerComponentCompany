package com.project.david.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="product")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private double price;
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;

	public Product(String name, double price, int quantity, Order order) {
		super();
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.order = order;
	}

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
