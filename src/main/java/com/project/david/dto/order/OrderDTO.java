package com.project.david.dto.order;

import java.time.LocalDate;
import java.util.List;

import com.project.david.dto.product.ProductDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
	// 與 Order 的差別在於 Employee 和 EmployeeId
	private Integer id;
	private LocalDate orderDate;
	private double totalAmount;
	private Integer employeeId;
	private List<ProductDTO> products;
	
	
	public OrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public OrderDTO(Integer id, LocalDate orderDate, double totalAmount, Integer employeeId, List<ProductDTO> products) {
		super();
		this.id = id;
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.employeeId = employeeId;
		this.products = products;
	}
}
