package com.project.david.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="`orders`")
public class Order {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private LocalDate orderDate;
	private double totalAmount;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="employee_id")
	@JsonBackReference// 防止遞歸循環，並解決序列化、反序列化問題
	private Employee employee;
	
	@OneToMany(mappedBy="order",cascade=CascadeType.ALL)
	@JsonManagedReference// 防止遞歸循環，並解決序列化、反序列化問題
	private Set<Product> products;

	public Order(LocalDate orderDate, double totalAmount,Employee employee) {
		super();
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.employee=employee;
	}

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
