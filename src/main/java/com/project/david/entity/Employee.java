package com.project.david.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * 	員工管理系統 : 
 * 		員工屬性 :
 * 			員工編號(Id)
 * 			員工姓名(name)
 * 			員工帳號(username)
 * 			員工密碼(password)
 * 			員工職位(position) chairman president manager leader staff
 * 			員工部門(department) AD行政部 BD銷售部 PD企劃部
 */
@Getter
@Setter
@Entity
@Table(name="employees")
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String username;
	private String password;
	private String position;
	private String department;
	
	@OneToMany(mappedBy="employee",cascade=CascadeType.ALL)
	@JsonManagedReference// 防止遞歸循環，並解決序列化、反序列化問題
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
