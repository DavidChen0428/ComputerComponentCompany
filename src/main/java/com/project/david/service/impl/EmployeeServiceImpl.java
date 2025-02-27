package com.project.david.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.EmployeeDaoImpl;
import com.project.david.entity.Employee;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeDaoImpl employeeDaoImpl;

	@Override
	public void addEmployee(Employee employee) throws ServiceException {
		try {
			employeeDaoImpl.create(employee);
		} catch (DAOException e) {
			throw new ServiceException("addEmployee():新增員工失敗" + e.getMessage(), e);
		}
	}

	@Override
	public List<Employee> selectAllEmployee() throws ServiceException {
		try {
			return employeeDaoImpl.findAll();
		} catch (DAOException e) {
			throw new ServiceException("selectAllEmployee():查詢員工失敗" + e.getMessage(), e);
		}
	}

	@Override
	public Employee selectEmployeeById(Integer id) throws ServiceException {
		try {
			return employeeDaoImpl.findOne(id);
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeById():查詢員工失敗" + e.getMessage(), e);
		}
	}

	@Override
	public Employee selectEmployeeByName(String name) throws ServiceException {
		try {
			return employeeDaoImpl.findOne(name);
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeByName():查詢員工失敗" + e.getMessage(), e);
		}
	}

	@Override
	public List<Employee> selectEmployeeByPosition(String position) throws ServiceException {
		try {
			return employeeDaoImpl.findSome(position);
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeByPosition():查詢員工失敗" + e.getMessage(), e);
		}
	}

	@Override
	public List<Employee> selectEmployeeByDepartment(String department) throws ServiceException {
		try {
			return employeeDaoImpl.findSome(department);
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeByDepartment():查詢員工失敗" + e.getMessage(), e);
		}
	}

	@Override
	public Employee loginEmployee(String username, String password) throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(username);
			if (!password.equals(employee.getPassword())) {
				throw new ServiceException("loginEmployee():密碼錯誤");
			}
			return employee;
		} catch (DAOException e) {
			throw new ServiceException("loginEmployee():帳號錯誤" + e.getMessage(), e);
		}
	}

	@Override
	public boolean checkUsernameBeenUsed(String username) throws ServiceException {
		try {
			return employeeDaoImpl.existsByUsername(username);
		} catch (DAOException e) {
			throw new ServiceException("checkUsernameBeenUsed():檢查用戶名出錯:" + e.getMessage(), e);
		}
	}

	@Override
	public void updateEmployee(Integer id, String name, String password, String position, String department)
			throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(id);
			employee.setName(name);
			employee.setPassword(password);
			employee.setPosition(position);
			employee.setDepartment(department);
			employeeDaoImpl.update(employee);
		} catch (DAOException e) {
			throw new ServiceException("updateEmployee():出現數據庫訪問錯誤: " + e.getMessage(), e);
		}

	}

	@Override
	public void deleteEmployee(String username, String password) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
