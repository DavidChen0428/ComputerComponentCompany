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
import org.springframework.web.bind.annotation.RestController;

import com.project.david.dto.order.OrderDTO;
import com.project.david.entity.Employee;
import com.project.david.entity.Order;
import com.project.david.service.OrderService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/addOrder")
	public ResponseEntity<Map<String, String>> addOrder(@RequestBody OrderDTO orderDTO,HttpSession session) {
		Map<String, String> response = new HashMap<>();
		try {
			Employee loggedInEmployee=(Employee) session.getAttribute("Emp");
			orderDTO.setEmployeeId(loggedInEmployee.getId());
			orderService.addOrder(orderDTO);
			response.put("message", "add order success");
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (ServiceException e) {
			response.put("message", "add order failed: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/allOrder")
	public ResponseEntity<List<Order>> selectAllOrder(HttpSession session) {
		try {
			List<Order> list = orderService.selectAllOrder();
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/findOrder/id={id}")
	public ResponseEntity<Order> selectOrderById(@PathVariable("id") Integer id) {
		try {
			Order order = orderService.selectOrderById(id);
			return new ResponseEntity<>(order, HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/updateOrder")
	public ResponseEntity<Map<String, String>> updateOrder(@RequestBody OrderDTO orderDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			orderService.updateOrder(orderDTO);
			response.put("message", "Order updated successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "Orderupdate failed: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/deleteOrder/id={id}")
	public ResponseEntity<Map<String,String>> deleteOrder(@PathVariable Integer id){
		Map<String,String> response=new HashMap<>();
		try {
			orderService.deleteOrderById(id);
			response.put("message", "Order deleted successfully");
			return new ResponseEntity<>(response,HttpStatus.OK);
		}catch(ServiceException e) {
			response.put("message", "Order delete fail: "+e.getMessage());
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
	}

}
