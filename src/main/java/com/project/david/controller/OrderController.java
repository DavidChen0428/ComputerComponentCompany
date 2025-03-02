package com.project.david.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.project.david.dto.EmployeeDTO;
import com.project.david.dto.OrderDTO;
import com.project.david.service.OrderService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

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

	// 2. 獲取所有訂單
	@GetMapping("/allOrder")
	public ResponseEntity<?> selectAllOrder(HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		
		try {
			List<OrderDTO> orders = orderService.selectAllOrder();
			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 3.獲取當前登入員工的所有訂單
	@GetMapping("/findOrder/myOrder")
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
			OrderDTO orderDTO = orderService.selectOrderById(orderId);
			return new ResponseEntity<>(orderDTO, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 5. 更新訂單(只能更新自己的訂單)
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

	// 6. 刪除訂單(只能删除自己的訂單。)
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
		
		try {
			List<OrderDTO> orders = orderService.selectOrderByDate(date);
			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 8. 根據日期範圍查詢訂單(僅限 chairman 使用)
	@GetMapping("/findOrder/dateBetween")
	public ResponseEntity<?> getOrdersByDateRange(@RequestParam String startDate, @RequestParam String endDate,
			HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO employeeDTO = (EmployeeDTO) session.getAttribute("Emp");
		if (employeeDTO == null) {
			response.put("message", "please login first");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		try {
			List<OrderDTO> orders = orderService.selectOrderByDateBetween(startDate, endDate);
			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
