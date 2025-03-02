package com.project.david.service;

import java.util.List;

import com.project.david.dto.OrderDTO;

public interface OrderService {
	// create
	OrderDTO addOrder(OrderDTO orderDTO,Integer employeeId) throws ServiceException;
	
	// read
	List<OrderDTO> selectAllOrder() throws ServiceException;
	List<OrderDTO> selectOrderForEmployee(Integer employeeId) throws ServiceException;
	OrderDTO selectOrderById(Integer id) throws ServiceException;
	List<OrderDTO> selectOrderByDate(String date) throws ServiceException;
	List<OrderDTO> selectOrderByDateBetween(String startdate,String enddate) throws ServiceException;
	
	// update
	OrderDTO updateOrder(Integer orderId,OrderDTO orderDTO,Integer employeeId) throws ServiceException;
	
	// delete
	void deleteOrder(Integer orderId, Integer employeeId) throws ServiceException;
	
}
