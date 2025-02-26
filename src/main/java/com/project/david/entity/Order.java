package com.project.david.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Entity;
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
@Table(name="order")
public class Order {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private LocalDate orderDate;
	private double totalAmount;
	
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	@OneToMany(mappedBy="order")
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
