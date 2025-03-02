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

import com.project.david.dto.product.ProductDTO;
import com.project.david.entity.Product;
import com.project.david.service.OrderService;
import com.project.david.service.ProductService;
import com.project.david.service.ServiceException;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@PostMapping("/addProduct")
	public ResponseEntity<Map<String, String>> addProduct(ProductDTO productDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			productService.addProduct(productDTO);
			response.put("message", "Add product successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Add product fail: "+e.getMessage());
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findProduct/all")
	public ResponseEntity<List<Product>> allProduct(){
		try {
			List<Product> list = productService.selectAllProduct();
			return new ResponseEntity<>(list,HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findProduct/id={id}")
	public ResponseEntity<Product> selectProductById(@PathVariable("id") Integer id){
		try {
			Product product = productService.selectProductById(id);
			return new ResponseEntity<>(product,HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findProduct/name={name}")
	public ResponseEntity<List<Product>> selectProductByName(@PathVariable("name") String name){
		try {
			List<Product> list = productService.selectProductByName(name);
			return new ResponseEntity<>(list,HttpStatus.OK);
		} catch (ServiceException e) {
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("updateProduct")
	public ResponseEntity<Map<String,String>> updateProduct(@RequestBody ProductDTO productDTO){
		Map<String,String> response=new HashMap<>();
		try {
			productService.updateProduct(productDTO);
			response.put("message", "Update product success");
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Update product fail: "+e.getMessage());
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("deleteProduct/id={id}")
	public ResponseEntity<Map<String,String>> deleteProductById(@PathVariable Integer id){
		Map<String,String> response=new HashMap<>();
		try {
			productService.deleteProductById(id);
			response.put("message", "Delete product success");
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Delete product fail: "+e.getMessage());
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
}
