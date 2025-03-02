package com.project.david.service;

import java.util.List;

import com.project.david.dto.EmployeeDTO;
import com.project.david.dto.ProductDTO;

public interface ProductService {
	// create 
	// 新增新產品
	ProductDTO addProduct(ProductDTO productDTO, Integer orderId) throws ServiceException;
	
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
	ProductDTO updateProduct(Integer productId, ProductDTO productDTO) throws ServiceException;
	
	// delete
	// 刪除產品
	void deleteProduct(Integer productId) throws ServiceException;
	
	// 判別式
	// 檢查員工是否有權限修改訂單
	public boolean canModifyOrder(EmployeeDTO employeeDTO, Integer orderId) throws ServiceException;
	// 檢查員工是否有權限查看產品
	public boolean canViewProduct(EmployeeDTO employeeDTO,ProductDTO productDTO) throws ServiceException;
	// 檢查員工是否有權秀修改或刪除指定的產品
	public boolean canModifyProduct(EmployeeDTO employee,Integer productId) throws ServiceException;
	// 檢查員工是否有權查看指定訂單的產品列表
	public boolean canViewOrderProduct(EmployeeDTO employee, Integer orderId) throws ServiceException;
	// 檢查員工權限，返回匹配的產品列表
	public List<ProductDTO> getProductsByNameAndEmployee(String name, EmployeeDTO employee) throws ServiceException;
	
}
