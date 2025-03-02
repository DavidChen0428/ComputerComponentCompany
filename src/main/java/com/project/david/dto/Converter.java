package com.project.david.dto;

import java.util.List;
import java.util.stream.Collectors;

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
		if (employee == null) {
			return null;
		}
		EmployeeDTO dto = new EmployeeDTO();
		dto.setId(employee.getId());
		dto.setName(employee.getName());
		dto.setUsername(employee.getUsername());
		dto.setPosition(employee.getPosition());
		dto.setDepartment(employee.getDepartment());
		return dto;
	}

	// EmployeeDTO -> Employee
	public static Employee convertToEmployeeByEmployeeDTO(EmployeeDTO employeeDTO) {
		if (employeeDTO == null) {
			return null;
		}
		Employee employee = new Employee();
		employee.setId(employeeDTO.getId());
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
	// 避免傳入 Employee 物件，傳遞 employeeId
	public static OrderDTO convertToOrderDTO(Order order) {
		if (order == null) {
			return null;
		}
		List<ProductDTO> products = order.getProducts().stream().map(Converter::convertToProductDTO)
				.collect(Collectors.toList());
		OrderDTO dto = new OrderDTO();
		dto.setId(order.getId());
		dto.setOrderDate(order.getOrderDate());
		dto.setTotalAmount(order.getTotalAmount());
		dto.setEmployeeId(order.getEmployee() != null ? order.getEmployee().getId() : null);
		dto.setProducts(products);
		return dto;
	}

	// OrderDTO -> Order
	// 避免循環依賴 -> 不獲取完整 Order 和 Employee 物件，只設置Id
	public static Order convertToOrder(OrderDTO orderDTO) {
		if (orderDTO == null) {
			return null;
		}
		Order order = new Order();
		order.setId(orderDTO.getId());
		// orderDate通常在創建時設置為當前日期，可根據需求調整
		order.setOrderDate(orderDTO.getOrderDate());
		// totalAmount 不需要手動設置，由 product 計算得出

		// 設置關聯 Employee -> 由 employeeId 取得 Employee
		// 這裡只有設置 employeeId，需要在 Service 或 Controller 中處理
		Employee employee = new Employee();
		employee.setId(orderDTO.getEmployeeId());
		order.setEmployee(employee);

		// 将 ProductDTO 列表转换为 Product 列表
		List<Product> products = orderDTO.getProducts().stream().map(Converter::convertToProduct)
				.collect(Collectors.toList());

		// 設置每個產品的關聯訂單
		for (Product product : products) {
			product.setOrder(order);
		}
		order.setProducts(products);

		// 重新计算 totalAmount
		order.recalculateTotalAmount();
		return order;
	}

	/*
	 * Product 類的 DTO 轉換器
	 */

	// Product -> ProductDTO
	// 避免傳入 Order 物件，只傳遞 orderId
	public static ProductDTO convertToProductDTO(Product product) {
		if (product == null) {
			return null;
		}
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
		if(productDTO==null) {
			return null;
		}
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setQuantity(productDTO.getQuantity());
		
		// 設置關聯的 Order ，需要根據 orderId 獲取 Order 實體
		// 此處只設置 orderId，需要在 Service 或 Controller 中處理
		Order order=new Order();
		order.setId(productDTO.getOrderId());
		product.setOrder(order);
		return product;
	}

}
