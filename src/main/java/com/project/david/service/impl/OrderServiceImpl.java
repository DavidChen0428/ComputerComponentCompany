package com.project.david.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.EmployeeDaoImpl;
import com.project.david.dao.impl.jpa.OrderDaoImpl;
import com.project.david.dao.impl.jpa.ProductDaoImpl;
import com.project.david.dto.Converter;
import com.project.david.dto.OrderDTO;
import com.project.david.entity.Employee;
import com.project.david.entity.Order;
import com.project.david.entity.Product;
import com.project.david.service.OrderService;
import com.project.david.service.ServiceException;

// 商業邏輯處理
@Service
public class OrderServiceImpl implements OrderService {
	private final OrderDaoImpl orderDaoImpl;
	private final EmployeeDaoImpl employeeDaoImpl;


	public OrderServiceImpl(OrderDaoImpl orderDaoImpl, EmployeeDaoImpl employeeDaoImpl, ProductDaoImpl productDaoImpl) {
		super();
		this.orderDaoImpl = orderDaoImpl;
		this.employeeDaoImpl = employeeDaoImpl;
	}

	// 創建新訂單
	@Transactional
	@Override
	public OrderDTO addOrder(OrderDTO orderDTO, Integer employeeId) throws ServiceException {
		if(!employeeId.equals(orderDTO.getEmployeeId())) {
			throw new ServiceException("addOrder(): 無法為其他員工新增訂單");
		}
		try {
			// 查找員工 -> 員工不存在會拋出異常(DAOException)
			Employee employee = employeeDaoImpl.findOne(employeeId);

			// 創建 Order 物件，設置所屬 Employee
			Order order = new Order(employee);

			// 將 OrderDTO 內的 products 資料型態從 ProductDTO 轉為 Product
			List<Product> products = orderDTO.getProducts().stream().map(Converter::convertToProduct)
					.collect(Collectors.toList());
			// 為每個 product 設置所屬 Order
			products.forEach(product -> product.setOrder(order));
			// 將設好的 products 裝進 Order 內
			order.setProducts(products);

			// 計算訂單總金額
			order.recalculateTotalAmount();

			// 將訂單保存於數據庫內
			orderDaoImpl.create(order);

			// 返回值
			return Converter.convertToOrderDTO(order);
		} catch (DAOException e) {
			throw new ServiceException("addOrder(): 數據訪問層錯誤:" + e.getMessage(), e);
		}

	}

	// 查詢所有訂單()
	@Override
	public List<OrderDTO> selectAllOrder() throws ServiceException {
		try {
			List<Order> orders=orderDaoImpl.findAll();
			return orders.stream().map(Converter::convertToOrderDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectAllOrder():資料庫訪問層錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public List<OrderDTO> selectOrderForEmployee(Integer employeeId) throws ServiceException {
		List<Order> orders;
		try {
			orders = orderDaoImpl.findSome(employeeId);
			return orders.stream().map(Converter::convertToOrderDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectOrderForEmployee(): 數據處理錯誤 " + e.getMessage(), e);
		}
	}

	@Override
	public OrderDTO selectOrderById(Integer id) throws ServiceException {
		try {
			return Converter.convertToOrderDTO(orderDaoImpl.findOne(id));
		} catch (DAOException e) {
			throw new ServiceException("selectOrderById(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}

	}

	@Override
	public List<OrderDTO> selectOrderByDate(String date) throws ServiceException {
		try {
			List<Order> orders=orderDaoImpl.findSome(date);
			return orders.stream().map(Converter::convertToOrderDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectOrderByDate(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

	@Override
	public List<OrderDTO> selectOrderByDateBetween(String startdate, String enddate) throws ServiceException {
		try {
			List<Order> orders=orderDaoImpl.findSome(startdate, enddate);
			return orders.stream().map(Converter::convertToOrderDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectOrderByDateBetween(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

	// 修改訂單
	@Transactional
	@Override
	public OrderDTO updateOrder(Integer orderId, OrderDTO orderDTO, Integer employeeId) throws ServiceException {
		try {
			Order order = orderDaoImpl.findOne(orderId);
			if(!order.getEmployee().getId().equals(employeeId)) {
				throw new ServiceException("updateOrder(): 無法為其他員工修改訂單");
			}
			order.setOrderDate(orderDTO.getOrderDate());
			List<Product> products = orderDTO.getProducts().stream().map(Converter::convertToProduct)
					.collect(Collectors.toList());

			products.forEach(product -> product.setOrder(order));

			order.setProducts(products);
			order.recalculateTotalAmount();
			orderDaoImpl.update(order);
			return Converter.convertToOrderDTO(order);
		} catch (DAOException e) {
			throw new ServiceException("updateOrder(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public void deleteOrder(Integer orderId, Integer employeeId) throws ServiceException {

		try {
			Order order = orderDaoImpl.findOne(orderId);
			if(!order.getEmployee().getId().equals(employeeId)) {
				throw new ServiceException("deleteOrder(): 無法為其他員工刪除訂單");
			}
			orderDaoImpl.delete(order);
		} catch (DAOException e) {
			throw new ServiceException("deleteOrder(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}

	}

}
