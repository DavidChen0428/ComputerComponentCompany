package com.project.david.service;

import java.util.List;

import com.project.david.dto.product.ProductDTO;
import com.project.david.entity.Product;

public interface ProductService {
	// create
	void addProduct(ProductDTO productDTO) throws ServiceException;
	
	// read
	List<Product> selectAllProduct() throws ServiceException;
	Product selectProductById(Integer id) throws ServiceException;
	List<Product> selectProductByName(String name) throws ServiceException;
	
	// update
	void updateProduct(ProductDTO productDTO) throws ServiceException;
	
	// delete
	void deleteProductById(Integer id) throws ServiceException;
	
}
