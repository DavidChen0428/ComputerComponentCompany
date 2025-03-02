package com.project.david.service;

import java.util.List;

import com.project.david.dto.order.OrderDTO;
import com.project.david.entity.Order;

public interface OrderService {
	// create
	OrderDTO addOrder(OrderDTO orderDTO,Integer employeeId) throws ServiceException;
	
	// read
	List<Order> selectAllOrder() throws ServiceException;
	Order selectOrderById(Integer id) throws ServiceException;
	List<Order> selectOrderByDate(String date) throws ServiceException;
	List<Order> selectOrderByDateBetween(String startdate,String enddate) throws ServiceException;
	
	// update
	void updateOrder(OrderDTO orderDTO) throws ServiceException;
	
	// delete
	void deleteOrderById(Integer id) throws ServiceException;
	
}
