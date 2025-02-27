package com.project.david.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="employee")
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String username;
	private String password;
	private String position;
	private String department;
	
	@OneToMany(mappedBy="employee")
	private Set<Order> orders;

	public Employee(String name, String username, String password, String position, String department) {
		super();
		this.name = name;
		this.username = username;
		this.password = password;
		this.position = position;
		this.department = department;
	}

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
}
