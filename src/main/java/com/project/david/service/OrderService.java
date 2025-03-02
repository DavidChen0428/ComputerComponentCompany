package com.project.david.service;

import java.util.List;

import com.project.david.dto.OrderDTO;
import com.project.david.entity.Order;

public interface OrderService {
	// create
	OrderDTO addOrder(OrderDTO orderDTO,Integer employeeId) throws ServiceException;
	
	// read
	List<Order> selectAllOrder() throws ServiceException;
	List<OrderDTO> selectOrderForEmployee(Integer employeeId) throws ServiceException;
	Order selectOrderById(Integer id) throws ServiceException;
	List<Order> selectOrderByDate(String date) throws ServiceException;
	List<Order> selectOrderByDateBetween(String startdate,String enddate) throws ServiceException;
	
	// update
	OrderDTO updateOrder(Integer orderId,OrderDTO orderDTO,Integer employeeId) throws ServiceException;
	
	// delete
	void deleteOrder(Integer orderId, Integer employeeId) throws ServiceException;
	
}
