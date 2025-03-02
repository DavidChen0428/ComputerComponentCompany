package com.project.david.dao.impl.jpa;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionException;

import com.project.david.dao.BaseDAO;
import com.project.david.dao.DAOException;
import com.project.david.entity.Order;

// 數據處理邏輯
@Repository
public class OrderDaoImpl implements BaseDAO<Order> {
	@Autowired
	OrderRepository orderRepository;

	@Override
	public void create(Order data) throws DAOException {
		try {
			orderRepository.save(data);
		} catch (DataIntegrityViolationException e) {
			throw new DAOException("create():數據違反約束條件: " + e.getMessage(), e);
		} catch (ConstraintViolationException e) {
			throw new DAOException("create():數據違反約束條件: " + e.getMessage(), e);
		} catch (TransactionException e) {
			throw new DAOException("create():事務操作失敗: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new DAOException("create():非法參數: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("create():未知錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public Order findOne(Object key) throws DAOException {
		if (key instanceof Integer keyInt) {
			Optional<Order> optionalOrder = orderRepository.findById(keyInt);
			if (optionalOrder.isPresent()) {
				return optionalOrder.get();
			} else {
				throw new DAOException("findOne():沒有符合條件的訂單" + key.toString());
			}
		}
		throw new DAOException("findOne():無效的key參數類型: " + key.getClass().getName());
	}

	@Override
	public List<Order> findAll() throws DAOException {
		try {
			return orderRepository.findAll();
		} catch (DataAccessException e) {
			throw new DAOException("findAll():數據訪問失敗: " + e.getMessage(), e);
		} catch (TransactionException e) {
			throw new DAOException("findAll():事務操作失敗: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new DAOException("findAll():非法參數: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("findAll():未知錯誤: " + e.getMessage(), e);
		}
	}

	// 找單一一天的資料
	@Override
	public List<Order> findSome(Object key) throws DAOException {
		if (key instanceof LocalDate orderDate) {
			try {
				if (orderRepository.existsByOrderDate(orderDate)) {
					return orderRepository.findByOrderDate(orderDate);
				}
				throw new DAOException("findSome():沒有符合日期內的訂單" + orderDate.toString());
			} catch (DataAccessException e) {
				throw new DAOException("findSome(): 數據訪問失敗: " + e.getMessage(), e);
			} catch (Exception e) {
				throw new DAOException("findSome(): 未知錯誤: " + e.getMessage(), e);
			}
		}
		if (key instanceof Integer keyInt) {
			try {
				if (orderRepository.existsByEmployeeId(keyInt)) {
					return orderRepository.findByEmployeeId(keyInt);
				}
				throw new DAOException("findSome():沒有符合員工編號的訂單" + keyInt.toString());
			} catch (DataAccessException e) {
				throw new DAOException("findSome(): 數據訪問失敗: " + e.getMessage(), e);
			} catch (Exception e) {
				throw new DAOException("findSome(): 未知錯誤: " + e.getMessage(), e);
			}
		}
		throw new DAOException("findSome(): 無效的 key 參數類型: " + key.getClass().getName());
	}

	// 找日期區間的資料
	public List<Order> findSome(Object key1, Object key2) throws DAOException {
		if (key1 instanceof LocalDate startDate && key2 instanceof LocalDate endDate) {
			if (startDate.isAfter(endDate)) {
				throw new DAOException(
						"findSome(): 起始日期不能晚於結束日期: " + startDate.toString() + " 到 " + endDate.toString());
			}
			try {
				List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
				if (orders.isEmpty()) {
					throw new DAOException(
							"findSome(): 沒有符合日期區間內的訂單: " + startDate.toString() + " 到 " + endDate.toString());
				}
				return orderRepository.findByOrderDateBetween(startDate, endDate);
			} catch (DataAccessException e) {
				throw new DAOException("findSome(): 數據訪問失敗: " + e.getMessage(), e);
			} catch (Exception e) {
				throw new DAOException("findSome(): 未知錯誤: " + e.getMessage(), e);
			}
		}
		throw new DAOException(
				"findSome(): 無效的 key 參數類型: " + key1.getClass().getName() + "/" + key2.getClass().getName());
	}

	@Override
	public void update(Order data) throws DAOException {
		try {
			if (data.getId() == null) {
				throw new DAOException("update():訂單ID不能為空");
			}
			orderRepository.save(data);
		} catch (DataIntegrityViolationException e) {
			throw new DAOException("update():數據違反約束條件: " + e.getMessage(), e);
		} catch (ConstraintViolationException e) {
			throw new DAOException("update():數據違反約束條件: " + e.getMessage(), e);
		} catch (TransactionException e) {
			throw new DAOException("update():事務操作失敗: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new DAOException("update():非法參數: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("update():未知錯誤: " + e.getMessage(), e);
		}
	}

	// delete()目前只允許Integer參數
	@Override
	public void delete(Object key) throws DAOException {
		try {
			if (key instanceof Integer) {
				orderRepository.deleteById((Integer) key);
			} else if (key instanceof Order) {
				orderRepository.delete((Order) key);
			} else {
				throw new DAOException("delete():無效的key參數類型: " + key.getClass().getName());
			}

		} catch (EmptyResultDataAccessException e) {
			throw new DAOException("delete():刪除失敗，沒有找到對應的記錄: " + key.toString(), e);
		} catch (DataAccessException e) {
			throw new DAOException("delete():數據訪問失敗: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("delete():未知錯誤: " + e.getMessage(), e);
		}
	}

}
