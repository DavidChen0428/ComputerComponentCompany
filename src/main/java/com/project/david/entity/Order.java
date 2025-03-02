package com.project.david.entity;

import java.time.LocalDate;
import java.util.List;

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

/*
 * 	訂單管理系統 : 
 * 		訂單屬性 :
 * 			訂單編號(id)
 * 			訂單日期(orderDate)
 * 			總價(totalAmount)
 */

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
	
	@OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true)
	@JsonManagedReference// 防止遞歸循環，並解決序列化、反序列化問題
	private List<Product> products;

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
	
	// 為了設定totalAmount會隨著Product的新增刪除變動
	public void addProduct(Product product) {
		products.add(product);
		product.setOrder(this);
		recalculateTotalAmount();
	}
	
	public void removeProduct(Product product) {
		products.remove(product);
		product.setOrder(null);
		recalculateTotalAmount();
	}
	
	public void recalculateTotalAmount() {
		double sum=0.0;
		for(Product product:products) {
			sum+=product.getPrice()*product.getQuantity();
		}
		this.totalAmount=sum;
	}
}
