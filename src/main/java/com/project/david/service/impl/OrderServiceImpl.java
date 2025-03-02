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
import com.project.david.dto.ConvertException;
import com.project.david.dto.order.OrderDTO;
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
	private final ProductDaoImpl productDaoImpl;

	public OrderServiceImpl(OrderDaoImpl orderDaoImpl, EmployeeDaoImpl employeeDaoImpl, ProductDaoImpl productDaoImpl) {
		super();
		this.orderDaoImpl = orderDaoImpl;
		this.employeeDaoImpl = employeeDaoImpl;
		this.productDaoImpl = productDaoImpl;
	}

	@Transactional
	@Override
	public OrderDTO addOrder(OrderDTO orderDTO,Integer employeeId) throws ServiceException {
		Employee employee=employeeDaoImpl.findOne(employeeId);
		Order order=new Order();
		order.setEmployee(employee);
		
		List<Product> products=orderDTO.getProducts().stream().map(Converter::convertToProduct).collect(Collectors.toList());
		
		try {
			Order order = Converter.convertToOrder(orderDTO, employeeDaoImpl, productDaoImpl);
			orderDaoImpl.create(order);
		} catch (DAOException e) {
			throw new ServiceException("addOrder(): 資料庫訪問層錯誤: " + e.getMessage(), e);
		} catch (ConvertException e) {
			throw new ServiceException("addOrder(): 資料型態轉換錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Order> selectAllOrder() throws ServiceException {
		try {
			return orderDaoImpl.findAll();
		} catch (DAOException e) {
			throw new ServiceException("selectAllOrder():資料庫訪問層錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public Order selectOrderById(Integer id) throws ServiceException {
		try {
			return orderDaoImpl.findOne(id);
		} catch (DAOException e) {
			throw new ServiceException("selectOrderById(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}

	}

	@Override
	public List<Order> selectOrderByDate(String date) throws ServiceException {
		try {
			return orderDaoImpl.findSome(date);
		} catch (DAOException e) {
			throw new ServiceException("selectOrderByDate(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

	@Override
	public List<Order> selectOrderByDateBetween(String startdate, String enddate) throws ServiceException {
		try {
			return orderDaoImpl.findSome(startdate, enddate);
		} catch (DAOException e) {
			throw new ServiceException("selectOrderByDateBetween(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

	@Override
	public void updateOrder(OrderDTO orderDTO) throws ServiceException {
		try {
			Order order = Converter.convertToOrder(orderDTO, employeeDaoImpl, productDaoImpl);
			orderDaoImpl.update(order);
		} catch (DAOException e) {
			throw new ServiceException("updateOrder(): 資料庫訪問層錯誤" + e.getMessage(), e);
		} catch (ConvertException e) {
			throw new ServiceException("updateOrder(): 資料型態轉換錯誤" + e.getMessage(), e);
		}
	}

	@Override
	public void deleteOrderById(Integer id) throws ServiceException {
		try {
			orderDaoImpl.delete(id);
		} catch (DAOException e) {
			throw new ServiceException("deleteOrderById(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

}
