package com.project.david.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.OrderDaoImpl;
import com.project.david.dao.impl.jpa.ProductDaoImpl;
import com.project.david.dto.Converter;
import com.project.david.dto.ProductDTO;
import com.project.david.entity.Order;
import com.project.david.entity.Product;
import com.project.david.service.ProductService;
import com.project.david.service.ServiceException;

@Service
public class ProductServiceImpl implements ProductService {
	private final ProductDaoImpl productDaoImpl;
	private final OrderDaoImpl orderDaoImpl;

	public ProductServiceImpl(ProductDaoImpl productDaoImpl, OrderDaoImpl orderDaoImpl) {
		super();
		this.productDaoImpl = productDaoImpl;
		this.orderDaoImpl = orderDaoImpl;
	}

	// 添加新產品
	@Transactional
	@Override
	public ProductDTO addProduct(ProductDTO productDTO, Integer orderId,Integer employeeId) throws ServiceException {
		if(!isOrderOwnedByEmployee(orderId,employeeId)) {
			throw new ServiceException("您沒有權限在他人訂單下添加產品。");
		}
		
		try {
			// 驗證orderId 是否存在
			Order order = orderDaoImpl.findOne(orderId);
			if (order == null) {
				throw new ServiceException("addProduct(): order doesn't exist. can't not add product.");
			}

			// 將 ProductDTO 轉成 Product
			Product product = Converter.convertToProduct(productDTO);
			// 新產品設置放置在哪個訂單裡面
			product.setOrder(order);

			// 把產品新增至資料庫內
			productDaoImpl.create(product);

			// 更新訂單的 TotalAmount
			order.getProducts().add(product);
			order.recalculateTotalAmount();
			orderDaoImpl.update(order);

			// 回傳保存後的產品資訊
			return Converter.convertToProductDTO(product);
		} catch (DAOException e) {
			throw new ServiceException("addProduct(): add product fail: " + e.getMessage(), e);
		}

	}

	@Override
	public ProductDTO selectProductById(Integer productId) throws ServiceException {
		try {
			// 調用DAO層查詢方法
			Product product = productDaoImpl.findOne(productId);
			// 轉換資料型態後輸出
			return Converter.convertToProductDTO(product);
		} catch (DAOException e) {
			throw new ServiceException("selectProductById(): select fail: " + e.getMessage(), e);
		}
	}

	@Override
	public List<ProductDTO> selectProductByOrderId(Integer orderId) throws ServiceException {
		try {
			// 驗證訂單是否存在
			Order order = orderDaoImpl.findOne(orderId);
			if (order == null) {
				throw new ServiceException("selectProductByOrderId(): Order doesn't exist.");
			}
			// 調用DAO層查詢方法
			List<Product> products = order.getProducts();
			// 轉換資料型態後輸出
			return products.stream().map(Converter::convertToProductDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectProductByOrderId(): get products fail: " + e.getMessage(), e);
		}

	}

	@Override
	public List<ProductDTO> selectProductByName(String name) throws ServiceException {
		try {
			// 調用DAO層查詢方法
			List<Product> products = productDaoImpl.findSome(name);
			// 轉換資料型態後輸出
			return products.stream().map(Converter::convertToProductDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectProductByName(): select fail: " + e.getMessage(), e);
		}
	}

	@Override
	public List<ProductDTO> selectAllProduct() throws ServiceException {
		try {
			// 調用DAO層查詢方法
			List<Product> products = productDaoImpl.findAll();
			// 轉換資料型態後輸出
			return products.stream().map(Converter::convertToProductDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectAllProduct(): select fail: " + e.getMessage(), e);
		}
	}

	// 修改產品
	@Transactional
	@Override
	public ProductDTO updateProduct(Integer productId, ProductDTO productDTO,Integer employeeId) throws ServiceException {
		if (!isProductOwnedByEmployee(productId, employeeId)) {
            throw new ServiceException("您沒有權限在他人訂單下修改產品。");
		}
	
		try {
			// 根據 productId 獲取 Product 物件
			Product product = productDaoImpl.findOne(productId);
			if (product == null) {
				throw new ServiceException("updateProduct(): Product doesn't exist.");
			}

			// 更新產品訊息
			if (isNotNullOrEmpty(productDTO.getName())) {
				product.setName(productDTO.getName());
			}
			if (productDTO.getPrice() > 0) {
				product.setPrice(productDTO.getPrice());
			}
			if (productDTO.getQuantity() > 0) {
				product.setQuantity(productDTO.getQuantity());
			}

			// 產品訊息儲存數據庫
			productDaoImpl.update(product);

			// 更新關聯訂單的totalAmount
			Order order = product.getOrder();
			order.recalculateTotalAmount();
			// 保存訂單
			orderDaoImpl.update(order);

			// 資料轉換後回傳
			return Converter.convertToProductDTO(product);
		} catch (DAOException e) {
			throw new ServiceException("updateProduct(): update fail: " + e.getMessage(), e);
		}

	}

	private boolean isNotNullOrEmpty(String str) {
		return str != null && !str.isEmpty();
	}

	@Transactional
	@Override
	public void deleteProduct(Integer productId,Integer employeeId) throws ServiceException {
		if (!isProductOwnedByEmployee(productId, employeeId)) {
            throw new ServiceException("您沒有權限刪除他人訂單下的產品");
        }
		
		try {
			// 根據 productId 找到 Product 物件
			Product product = productDaoImpl.findOne(productId);
			if (product == null) {
				throw new ServiceException("deleteProduct(): Product doesn't exist.");
			}

			// 從產品中調出 Order物件
			Order order = product.getOrder();

			// 删除產品
			productDaoImpl.delete(productId);

			// 更新訂單的 totalAmount
			order.getProducts().remove(product);
			order.recalculateTotalAmount();
			// 訂單資料保存
			orderDaoImpl.update(order);

		} catch (DAOException e) {
			throw new ServiceException("deleteProduct(): delete fail: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean isOrderOwnedByEmployee(Integer orderId, Integer employeeId) throws ServiceException {
		try {
			Order order = orderDaoImpl.findOne(orderId);
			if (order == null) {
				throw new ServiceException("isOrderOwnedByEmployee(): Order doesn't exist.");
			}
			return order.getEmployee().getId().equals(employeeId);
		} catch (DAOException e) {
			throw new ServiceException("檢查訂單歸屬失敗: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean isProductOwnedByEmployee(Integer productId, Integer employeeId) throws ServiceException {
		try {
			Product product = productDaoImpl.findOne(productId);
			if (product == null) {
				throw new ServiceException("isProductOwnedByEmployee(): 產品不存在");
			}
			return product.getOrder().getEmployee().getId().equals(employeeId);
		} catch (DAOException e) {
			throw new ServiceException("檢查產品歸屬失敗：" + e.getMessage(), e);

		}
	}
}
