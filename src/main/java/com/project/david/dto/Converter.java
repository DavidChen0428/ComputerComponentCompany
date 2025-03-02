package com.project.david.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.EmployeeDaoImpl;
import com.project.david.dao.impl.jpa.ProductDaoImpl;
import com.project.david.dto.employee.EmployeeDTO;
import com.project.david.dto.order.OrderDTO;
import com.project.david.dto.product.ProductDTO;
import com.project.david.entity.Employee;
import com.project.david.entity.Order;
import com.project.david.entity.Product;

public class Converter {
	/*
	 * Employee 類的 DTO 轉換器 Employee -> DTO 通常是要資料送出到客戶顯示 DTO -> Employee 新增 DTO ->
	 * 資料提取
	 */

	// Employee -> EmployeeRegisterDTO
	// 不包含密碼的訊息
	public static EmployeeDTO convertToEmployeeDTO(Employee employee) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setId(employee.getId());
		dto.setName(employee.getName());
		dto.setUsername(employee.getUsername());
		dto.setPosition(employee.getPosition());
		dto.setDepartment(employee.getDepartment());
		return dto;
	}

	// EmployeeRegisterDTO -> Employee
	public static Employee convertToEmployeeByEmployeeDTO(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		employee.setName(employeeDTO.getName());
		employee.setUsername(employeeDTO.getUsername());
		employee.setPassword(employeeDTO.getPassword());
		employee.setPosition(employeeDTO.getPosition());
		employee.setDepartment(employeeDTO.getDepartment());
		return employee;
	}

	/*
	 * Order 類的DTO轉換器 Order -> OrderDTO OrderDTO -> Order
	 */

	// Order -> OrderDTO
	public static OrderDTO convertToOrderDTO(Order order) {
		OrderDTO dto = new OrderDTO();
		dto.setId(order.getId());
		dto.setOrderDate(order.getOrderDate());
		dto.setTotalAmount(order.getTotalAmount());
		dto.setEmployeeId(order.getEmployee() != null ? order.getEmployee().getId() : null);
		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : order.getProducts()) {
			ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice(),
					product.getQuantity(),product.getOrder().getId());
			productDTOs.add(productDTO);
		}
		dto.setProducts(productDTOs);
		dto.setProducts(order.getProducts().stream().map(Converter::convertToProductDTO).collect(Collectors.toList()));

		return dto;
	}

	// OrderDTO -> Order
//	public static Order convertToOrder(OrderDTO orderDTO, EmployeeDaoImpl employeeDaoImpl,
//			ProductDaoImpl productDaoImpl) throws ConvertException {
//		Order order = new Order();
//		try {
//			order.setEmployee(employeeDaoImpl.findOne(orderDTO.getEmployeeId()));
//			order.setOrderDate(orderDTO.getOrderDate());
//			List<Product> products = new ArrayList<>();
//			for (OrderProductDTO productDTO : orderDTO.getProducts()) {
//				Product product = productDaoImpl.findOne(productDTO.getId());
//				products.add(product);
//			}
//			order.setProducts(products);
//		} catch (DAOException e) {
//			throw new ConvertException("convertToOrder():資料庫訪問層錯誤" + e.getMessage(), e);
//		}
//
//		return order;
//	}

	/*
	 * Product 類的 DTO 轉換器
	 */

	// Product -> ProductDTO
	public static ProductDTO convertToProductDTO(Product product) {
		ProductDTO dto = new ProductDTO();
		dto.setId(product.getId());
		dto.setName(product.getName());
		dto.setPrice(product.getPrice());
		dto.setQuantity(product.getQuantity());
		dto.setOrderId(product.getOrder() != null ? product.getOrder().getId() : null);
		return dto;
	}

	// ProductDTO -> Product
	public static Product convertToProduct(ProductDTO productDTO) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setQuantity(productDTO.getQuantity());
		if (productDTO.getOrderId() != null) {
			Order order = new Order();
			order.setId(productDTO.getOrderId());
			product.setOrder(order);
		} else {
			product.setOrder(null);
		}
		return product;
	}

}
