package com.project.david.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.project.david.service.ProductService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}

	private EmployeeDTO getCurrentEmployee(HttpSession session) {
		return (EmployeeDTO) session.getAttribute("Emp");
	}

	// 1. 添加新產品到指定訂單(員工自己下自己的單)
	@PostMapping("/addProduct/orderId={orderId}")
	public ResponseEntity<?> addProduct(@PathVariable("orderId") Integer orderId, @RequestBody ProductDTO productDTO,
			HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee(session);
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			// 只有訂單的創建者或 chairman 可以添加產品
			if (!productService.isOrderOwnedByEmployee(orderId, currentEmployee.getId())) {
				response.put("message", "you don't have permission to add product in others order.");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}
			productService.addProduct(productDTO, orderId, currentEmployee.getId());
			response.put("message", "add product successfully.");
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (ServiceException e) {
			response.put("message", "Add product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 2.獲取產品的訊息(所有員工)
	@GetMapping("/findProduct/productId={productId}")
	public ResponseEntity<?> selectProductById(@PathVariable("productId") Integer productId, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee(session);
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		try {
			ProductDTO productDTO = productService.selectProductById(productId);
			return new ResponseEntity<>(productDTO, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "select product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// 3. 获取指定订单的所有产品(所有員工)
	@GetMapping("/findProduct/orderId={orderId}")
	public ResponseEntity<?> selectProductByOrderId(@PathVariable Integer orderId, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee(session);
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		try {
			List<ProductDTO> products = productService.selectProductByOrderId(orderId);
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "select product in the order fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// 4. 修改產品(員工自己修改自己的)
	@PutMapping("updateProduct/productId={productId}")
	public ResponseEntity<?> updateProduct(@PathVariable("productId") Integer productId,
			@RequestBody ProductDTO productDTO, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee(session);
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		try {
			// 檢查產品是不是屬於當前用戶的訂單
			if (!productService.isProductOwnedByEmployee(productId, currentEmployee.getId())) {
				response.put("message", "You don't have permission to update this product.");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}
			productService.updateProduct(productId, productDTO, currentEmployee.getId());
			response.put("message", "Update product success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Update product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// 5. 刪除產品(員工自己的訂單自己刪除)
	@DeleteMapping("deleteProduct/productId={productid}")
	public ResponseEntity<?> deleteProductById(@PathVariable Integer productId, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee(session);
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}

		try {
			// 檢查產品是否屬於當前用戶的訂單
			if (!productService.isProductOwnedByEmployee(productId, currentEmployee.getId())) {
				response.put("message", "You don't have permission to delete this product.");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}
			productService.deleteProduct(productId, currentEmployee.getId());
			response.put("message", "Delete product success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Delete product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// 6. 根據產品名稱查詢產品(所有員工)
	@GetMapping("/findProduct/name={name}")
	public ResponseEntity<?> selectProductByName(@PathVariable("name") String name, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee(session);
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		// 根據權限返回數據

		try {
			List<ProductDTO> list = productService.selectProductByName(name);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "select product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// 7. 查詢所有產品(所有員工)
	@GetMapping("/findProduct/all")
	public ResponseEntity<?> allProduct(HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = getCurrentEmployee(session);
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		try {
			List<ProductDTO> products = productService.selectAllProduct();
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "select all product fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}
