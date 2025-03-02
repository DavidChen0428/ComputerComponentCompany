package com.project.david.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * 	商品管理 : 
 * 		商品屬性 :
 * 			商品編號(id)
 * 			商品名稱(name)
 * 			商品價格(price)
 * 			商品數量(quantity)
 */

@Getter
@Setter
@Entity
@Table(name="products")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private double price;
	private int quantity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id")
	@JsonBackReference// 防止遞歸循環，並解決序列化、反序列化問題
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
