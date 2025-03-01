package com.project.david.dto;

import java.util.ArrayList;
import java.util.List;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.EmployeeDaoImpl;
import com.project.david.dao.impl.jpa.ProductDaoImpl;
import com.project.david.dto.employee.EmployeeRegisterDTO;
import com.project.david.dto.order.OrderDTO;
import com.project.david.dto.order.OrderProductDTO;
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
	public static EmployeeRegisterDTO convertToRegisterDTO(Employee employee) {
		EmployeeRegisterDTO dto = new EmployeeRegisterDTO();
		dto.setName(employee.getName());
		dto.setUsername(employee.getUsername());
		dto.setPassword(employee.getPassword());
		dto.setPosition(employee.getPosition());
		dto.setDepartment(employee.getDepartment());
		return dto;
	}

	// EmployeeRegisterDTO -> Employee
	public static Employee convertToEmployeeByRegisterDTO(EmployeeRegisterDTO employeeRegisterDTO) {
		Employee employee = new Employee();
		employee.setName(employeeRegisterDTO.getName());
		employee.setUsername(employeeRegisterDTO.getUsername());
		employee.setPassword(employeeRegisterDTO.getPassword());
		employee.setPosition(employeeRegisterDTO.getPosition());
		employee.setDepartment(employeeRegisterDTO.getDepartment());
		return employee;
	}

	/*
	 * Order 類的DTO轉換器 Order -> OrderDTO OrderDTO -> Order
	 */

	// Order -> OrderDTO
	public static OrderDTO convertToOrderDTO(Order order) throws DTOException {
		OrderDTO dto = new OrderDTO();
		dto.setId(order.getId());
		dto.setOrderDate(order.getOrderDate());
		dto.setEmployeeId(order.getEmployee().getId());
		List<OrderProductDTO> productDTOs = new ArrayList<>();
		for (Product product : order.getProducts()) {
			OrderProductDTO productDTO = new OrderProductDTO(product.getId(), product.getName(), product.getPrice(),
					product.getQuantity());
			productDTOs.add(productDTO);
		}
		dto.setProducts(productDTOs);
		return dto;
	}
	// OrderDTO -> Order
	public static Order convertToOrder(OrderDTO orderDTO, EmployeeDaoImpl employeeDaoImpl,
			ProductDaoImpl productDaoImpl) throws DTOException {
		Order order = new Order();
		try {
			order.setEmployee(employeeDaoImpl.findOne(orderDTO.getEmployeeId()));
			order.setOrderDate(orderDTO.getOrderDate());
			List<Product> products = new ArrayList<>();
			for (OrderProductDTO productDTO : orderDTO.getProducts()) {
				Product product = productDaoImpl.findOne(productDTO.getId());
				products.add(product);
			}
			order.setProducts(products);
		} catch (DAOException e) {
			throw new DTOException("convertToOrder():資料庫訪問層錯誤" + e.getMessage(), e);
		}

		return order;
	}
	
	/*
	 * Product 類的 DTO 轉換器
	 */
	
	// ProductDTO -> Product
	public static Product convertToProduct(ProductDTO productDTO) throws DTOException{
		Product product=new Product();
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setQuantity(productDTO.getQuantity());
		if(productDTO.getOrderId()!=null) {
			Order order=new Order();
			order.setId(productDTO.getOrderId());
			product.setOrder(order);
		}else {
			product.setOrder(null);
		}
		return product;
	}
	
	// Product -> ProductDTO
	public static ProductDTO convertToProductDTO(Product product) throws DTOException{
		ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setOrderId(product.getOrder() != null ? product.getOrder().getId() : null);
        return productDTO;
	}
}
