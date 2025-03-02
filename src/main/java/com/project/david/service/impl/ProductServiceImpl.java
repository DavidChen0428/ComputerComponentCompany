package com.project.david.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.OrderDaoImpl;
import com.project.david.dao.impl.jpa.ProductDaoImpl;
import com.project.david.dto.Converter;
import com.project.david.dto.EmployeeDTO;
import com.project.david.dto.ProductDTO;
import com.project.david.entity.Employee;
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
	public ProductDTO addProduct(ProductDTO productDTO, Integer orderId) throws ServiceException {
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

	@Transactional
	@Override
	public ProductDTO updateProduct(Integer productId, ProductDTO productDTO) throws ServiceException {
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
	public void deleteProduct(Integer productId) throws ServiceException {
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
	public boolean canModifyOrder(EmployeeDTO employeeDTO, Integer orderId) throws ServiceException {
		try {
			// 根据 orderId 获取订单
			Order order = orderDaoImpl.findOne(orderId);
			if (order == null) {
				throw new ServiceException("canModifyOrder(): doesn't exist");
			}

			// 獲得訂單的創建者
			Employee orderOwner = order.getEmployee();

			// 檢查當前員工是否為訂單創建者或是職位為"chairman"
			return orderOwner.getId().equals(employeeDTO.getId())
					|| "chairman".equalsIgnoreCase(employeeDTO.getPosition());
		} catch (DAOException e) {
			throw new ServiceException("canModifyOrder(): modify fail" + e.getMessage(), e);
		}
	}

	@Override
	public boolean canViewProduct(EmployeeDTO employeeDTO, ProductDTO productDTO) throws ServiceException {
		try {
			// 根據 ProductDTO 找到 Order 物件，並確認關聯訂單存不存在
			Order order = orderDaoImpl.findOne(productDTO.getOrderId());
			if (order == null) {
				throw new ServiceException("canViewProduct(): Order doesn't exist.");
			}

			// 獲取訂單的創建者(員工)
			Employee orderOwner = order.getEmployee();

			// 檢查當前的員工是否為訂單創建者，或是該員工的職位為"chairman"
			boolean isOrderOwner = orderOwner.getId().equals(employeeDTO.getId());
			boolean isChairman = "chairman".equalsIgnoreCase(employeeDTO.getPosition());

			// 回傳檢查結果
			return isOrderOwner || isChairman;

		} catch (DAOException e) {
			throw new ServiceException("canViewProduct(): validate fail: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean canModifyProduct(EmployeeDTO employee, Integer productId) throws ServiceException {
		try {
			Product product = productDaoImpl.findOne(productId);
			if (product == null) {
				throw new ServiceException("canModifyProduct(): product doesn't exist.");
			}
			Order order = product.getOrder();
			return order.getEmployee().getId().equals(employee.getId())
					|| "chairman".equalsIgnoreCase(employee.getPosition());
		} catch (DAOException e) {
			throw new ServiceException("canModifyProduct(): validate fail: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean canViewOrderProduct(EmployeeDTO employee, Integer orderId) throws ServiceException {
		return canModifyOrder(employee,orderId);
	}

	@Override
	public List<ProductDTO> getProductsByNameAndEmployee(String name, EmployeeDTO employee) throws ServiceException {
		try {
	        List<Product> products = productDaoImpl.findSome(name);
	        return products.stream()
	                .filter(product -> {
	                    try {
	                        return canViewProduct(employee, Converter.convertToProductDTO(product));
	                    } catch (ServiceException e) {
	                        return false;
	                    }
	                })
	                .map(Converter::convertToProductDTO)
	                .collect(Collectors.toList());
	    } catch (DAOException e) {
	        throw new ServiceException("getProductByNameAndEmployee(): select fail: " + e.getMessage(), e);
	    }
	}

}
