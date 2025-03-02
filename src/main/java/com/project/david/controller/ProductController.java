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

import com.project.david.dto.EmployeeDTO;
import com.project.david.dto.ProductDTO;
import com.project.david.entity.Product;
import com.project.david.service.OrderService;
import com.project.david.service.ProductService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;
	private final HttpSession session;

	public ProductController(ProductService productService, HttpSession session) {
		super();
		this.productService = productService;
		this.session = session;
	}

	private EmployeeDTO getCurrentEmployee() {
		return (EmployeeDTO) session.getAttribute("Emp");
	}

	// 1. 添加新產品到指定訂單
	@PostMapping("/addProduct/order/{orderId}")
	public ResponseEntity<?> addProduct(@PathVariable("orderId") Integer orderId, @RequestBody ProductDTO productDTO) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee();
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			// 只有訂單的創建者或 chairman 可以添加產品
			if (!productService.canModifyOrder(currentEmployee, orderId)) {
				response.put("message", "you don't have permission to add product.");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}
			productService.addProduct(productDTO, orderId);
			response.put("message", "add product successfully.");
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (ServiceException e) {
			response.put("message", "Add product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/findProduct/all")
	public ResponseEntity<?> allProduct() {
		try {
			List<Product> list = productService.selectAllProduct();
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	// 2.獲取產品的訊息
	@GetMapping("/findProduct/productId={productId}")
	public ResponseEntity<?> selectProductById(@PathVariable("productId") Integer productId) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee();
		if(currentEmployee ==null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
		}
		
		ProductDTO productDTO=productService.selectProductById(productId);
		
		if(!productService.c)
		try {
			Product product = 
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findProduct/name={name}")
	public ResponseEntity<?> selectProductByName(@PathVariable("name") String name) {
		try {
			List<Product> list = productService.selectProductByName(name);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("updateProduct")
	public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			productService.updateProduct(productDTO);
			response.put("message", "Update product success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Update product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("deleteProduct/id={id}")
	public ResponseEntity<?> deleteProductById(@PathVariable Integer id) {
		Map<String, String> response = new HashMap<>();
		try {
			productService.deleteProductById(id);
			response.put("message", "Delete product success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Delete product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
