package com.project.david.service;

import java.util.List;

import com.project.david.dto.ProductDTO;

public interface ProductService {
	// create 
	// 新增新產品
	ProductDTO addProduct(ProductDTO productDTO, Integer orderId,Integer employeeId) throws ServiceException;
	
	// read 
	// 根據產品Id獲取產品訊息
	ProductDTO selectProductById(Integer productId) throws ServiceException;
	// 獲取特定訂單的所有產品
	List<ProductDTO> selectProductByOrderId(Integer orderId) throws ServiceException;
	// 根據產品名稱查找產品
	List<ProductDTO> selectProductByName(String name) throws ServiceException;
	// 獲取所有產品(僅供chairman使用)
	List<ProductDTO> selectAllProduct() throws ServiceException;
	
	// update 
	// 更新產品資訊
	ProductDTO updateProduct(Integer productId, ProductDTO productDTO,Integer employeeId) throws ServiceException;
	
	// delete
	// 刪除產品
	void deleteProduct(Integer productId,Integer employeeId) throws ServiceException;
	
	// 判別式
	// 檢查訂單是否屬於員工
	boolean isOrderOwnedByEmployee(Integer orderId, Integer employeeId) throws ServiceException;
	// 檢查產品是否屬於員工的訂單
	boolean isProductOwnedByEmployee(Integer productId, Integer employeeId) throws ServiceException;
	
}
