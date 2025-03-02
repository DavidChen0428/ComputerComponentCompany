package com.project.david.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.david.dto.Converter;
import com.project.david.dto.EmployeeDTO;
import com.project.david.dto.OrderDTO;
import com.project.david.entity.Order;
import com.project.david.service.EmployeeService;
import com.project.david.service.OrderService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
	private final OrderService orderService;
	private final EmployeeService employeeService;

	public OrderController(OrderService orderService, EmployeeService employeeService) {
		super();
		this.orderService = orderService;
		this.employeeService = employeeService;
	}

	// 1. 創建新訂單
	@PostMapping("/addOrder")
	public ResponseEntity<?> addOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
		Map<String, String> response = new HashMap<>();

		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			orderService.addOrder(orderDTO, employeeDTO.getId());
			response.put("message", "add Order successfully.");
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 2. 獲取所有訂單(僅 chairman 可用)
	@GetMapping("/allOrder")
	public ResponseEntity<?> selectAllOrder(HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (!"chairman".equalsIgnoreCase(employeeDTO.getPosition())) {
			response.put("message", "you don't have permission to look all orders.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			List<Order> orders = orderService.selectAllOrder();
			List<OrderDTO> orderDTOs = orders.stream().map(Converter::convertToOrderDTO).collect(Collectors.toList());
			return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 3.獲取當前員工的所有訂單
	@GetMapping("/findOrder/employee")
	public ResponseEntity<?> getOrdersForEmployee(HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			List<OrderDTO> orders = orderService.selectOrderForEmployee(employeeDTO.getId());
			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 4.根據訂單Id獲取訂單詳情
	@GetMapping("/findOrder/orderId={orderId}")
	public ResponseEntity<?> selectOrderById(@PathVariable("orderId") Integer orderId, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			Order order = orderService.selectOrderById(orderId);

			// 權限驗證
			if (!order.getEmployee().getId().equals(employeeDTO.getId())
					&& !"chairman".equalsIgnoreCase(employeeDTO.getPosition())) {
				response.put(null, "You dont't have permission to look this order.");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}

			OrderDTO orderDTO = Converter.convertToOrderDTO(order);
			return new ResponseEntity<>(orderDTO, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 5. 更新訂單
	@PutMapping("/updateOrder/orderId={orderId}")
	public ResponseEntity<?> updateOrder(@PathVariable("orderId") Integer orderId, @RequestBody OrderDTO orderDTO,
			HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		try {
			orderService.updateOrder(orderId, orderDTO, employeeDTO.getId());
			response.put("message", "Order updated successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// 6. 刪除訂單
	@DeleteMapping("/deleteOrder/orderId={orderId}")
	public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Integer orderId, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		try {
			orderService.deleteOrder(orderId, employeeDTO.getId());
			response.put("message", "Order deleted successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// 7.根據日期查詢訂單
	@GetMapping("/findOrder/date={date}")
	public ResponseEntity<?> selectOrderByDate(@PathVariable("date") String date, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (!"chairman".equalsIgnoreCase(employeeDTO.getPosition())) {
			response.put(null, "You dont't have permission to look this order.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			List<Order> orders = orderService.selectOrderByDate(date);
			List<OrderDTO> orderDTOs = orders.stream().map(Converter::convertToOrderDTO).collect(Collectors.toList());
			return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 8. 根據日期範圍查詢訂單
	@GetMapping("/findOrder/dateBetween")
	public ResponseEntity<?> getOrdersByDateRange(@RequestParam String startDate, @RequestParam String endDate,
			HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		if (!"chairman".equalsIgnoreCase(employeeDTO.getPosition())) {
			response.put(null, "You dont't have permission to look this order.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			List<Order> orders = orderService.selectOrderByDateBetween(startDate, endDate);
			List<OrderDTO> orderDTOs = orders.stream().map(Converter::convertToOrderDTO).collect(Collectors.toList());
			return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
