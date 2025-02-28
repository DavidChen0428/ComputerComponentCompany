package com.project.david.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.EmployeeDaoImpl;
import com.project.david.dto.employee.EmployeeConverter;
import com.project.david.dto.employee.EmployeeDeleteDTO;
import com.project.david.dto.employee.EmployeeLoginDTO;
import com.project.david.dto.employee.EmployeeRegisterDTO;
import com.project.david.dto.employee.EmployeeUpdateDTO;
import com.project.david.entity.Employee;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeDaoImpl employeeDaoImpl;

	@Override
	public void addEmployee(EmployeeRegisterDTO employeeRegisterDTO) throws ServiceException {
		Employee employee=EmployeeConverter.convertToEmployeeByRegisterDTO(employeeRegisterDTO);
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
	public Employee loginEmployee(EmployeeLoginDTO employeeLoginDTO) throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(employeeLoginDTO.getUsername());
			if (!(employeeLoginDTO.getPassword().equals(employee.getPassword()))) {
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
	public void updateEmployee(EmployeeUpdateDTO employeeUpdateDTO)
			throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(employeeUpdateDTO.getId());
			if (isNotNullOrEmpty(employeeUpdateDTO.getName())) {
				employee.setName(employeeUpdateDTO.getName());
			}
			if (isNotNullOrEmpty(employeeUpdateDTO.getPassword())) {
				employee.setPassword(employeeUpdateDTO.getPassword());
			}
			if (isNotNullOrEmpty(employeeUpdateDTO.getPosition())) {
				employee.setPosition(employeeUpdateDTO.getPosition());
			}
			if (isNotNullOrEmpty(employeeUpdateDTO.getDepartment())) {
				employee.setDepartment(employeeUpdateDTO.getDepartment());
			}
			employeeDaoImpl.update(employee);
		} catch (DAOException e) {
			throw new ServiceException("updateEmployee():數據庫訪問錯誤: " + e.getMessage(), e);
		}
	}
	
	// 判別EmployeeUpdateDTO物件內的參數裡面是否為空字串或是null
	private boolean isNotNullOrEmpty(String str) {
		return str!=null && !str.isEmpty();
	}

	// 傳入username,password進行資料驗證，再刪除
	@Override
	public void deleteEmployee(EmployeeDeleteDTO employeeDeleteDTO) throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(employeeDeleteDTO.getUsername());
			if (employeeDeleteDTO.getPassword() != employee.getPassword()) {
				throw new ServiceException("deleteEmployee():刪除密碼錯誤");
			}
			employeeDaoImpl.delete(employee.getId());
		} catch (DAOException e) {
			throw new ServiceException("deleteEmployee():數據庫訪問錯誤" + e.getMessage(), e);
		}
	}
}
