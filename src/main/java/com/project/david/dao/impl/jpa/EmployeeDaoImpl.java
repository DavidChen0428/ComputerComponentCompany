package com.project.david.dao.impl.jpa;

import java.util.ArrayList;
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
import com.project.david.entity.Employee;

// 數據處理邏輯在這裡做
/*
 *  create
 * 	findOne
 *  findSome
 *  findAll
 *  update
 *  delete
 *  existsByUsername(另外定義的方法，用來做新增員工時，檢查員工帳號是否重複)
 */
@Repository
public class EmployeeDaoImpl implements BaseDAO<Employee> {
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public void create(Employee data) throws DAOException {
		try {
			employeeRepository.save(data);
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

	// findOne()目前允許用Id(Integer)、name(String)、username(String)查詢
	@Override
	public Employee findOne(Object key) throws DAOException {
		if (key instanceof Integer) {
			Optional<Employee> optionalEmployee = employeeRepository.findById((Integer) key);
			if (optionalEmployee.isPresent()) {
				return optionalEmployee.get();
			} else {
				throw new DAOException("findOne():沒有符合條件的員工" + key.toString());
			}
		}
		if (key instanceof String keyStr) {
			if (employeeRepository.existsByName(keyStr)) {
				return employeeRepository.findByName(keyStr).get(0);
			}
			if (employeeRepository.existsByUsername(keyStr)) {
				return employeeRepository.findByUsername(keyStr).get(0);
			}
			throw new DAOException("findOne():沒有符合條件的員工" + keyStr);
		}
		throw new DAOException("findOne():無效的key參數類型: " + key.getClass().getName());
	}

	// findSome()目前允許用position(職位)、department(部門)查詢
	@Override
	public List<Employee> findSome(Object key) throws DAOException {
		if (!(key instanceof String)) {
			throw new DAOException("findSome(): 無效的key參數類型: " + key.getClass().getName());
		}

		String keyStr = (String) key;

		List<Employee> result = new ArrayList<>();

		if (employeeRepository.existsByPosition(keyStr)) {
			result.addAll(employeeRepository.findByPosition(keyStr));
		}

		if (employeeRepository.existsByDepartment(keyStr)) {
			result.addAll(employeeRepository.findByDepartment(keyStr));
		}

		if (result.isEmpty()) {
			throw new DAOException("findSome(): 沒有符合條件的員工");
		}

		return result;
	}

	// 尋找全部
	@Override
	public List<Employee> findAll() throws DAOException {
		try {
			return employeeRepository.findAll();
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

	// 修改，為了跟新增有所區別，會查詢是否有Id
	@Override
	public void update(Employee data) throws DAOException {
		try {
			if (data.getId() == null) {
				throw new DAOException("update():員工ID不能為空");
			}
			employeeRepository.save(data);
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
				employeeRepository.deleteById((Integer) key);
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
	
	public boolean existsByUsername(String username) throws DAOException{
		return employeeRepository.existsByUsername(username);
	}
}
