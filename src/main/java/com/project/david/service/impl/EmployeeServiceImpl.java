package com.project.david.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.EmployeeDaoImpl;
import com.project.david.dto.Converter;
import com.project.david.dto.EmployeeDTO;
import com.project.david.entity.Employee;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;

// 商業邏輯處理
@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeDaoImpl employeeDaoImpl;

	// 註冊新員工
	@Override
	public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) throws ServiceException {
		try {
			Employee employee = Converter.convertToEmployeeByEmployeeDTO(employeeDTO);

			employeeDaoImpl.create(employee);
			return Converter.convertToEmployeeDTO(employee);
		} catch (DAOException e) {
			throw new ServiceException("addEmployee():新增員工失敗" + e.getMessage(), e);
		}
	}

	// 獲取所有員工資訊(僅限chairman使用)
	@Override
	public List<EmployeeDTO> selectAllEmployee() throws ServiceException {
		try {
			List<Employee> employees = employeeDaoImpl.findAll();
			return employees.stream().map(Converter::convertToEmployeeDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectAllEmployee():查詢員工失敗" + e.getMessage(), e);
		}
	}

	// 根據Id獲取員工資料
	@Override
	public EmployeeDTO selectEmployeeById(Integer id) throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(id);
			return Converter.convertToEmployeeDTO(employee);
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeById():查詢員工失敗" + e.getMessage(), e);
		}
	}

	// 根據姓名獲取員工資料
	@Override
	public EmployeeDTO selectEmployeeByName(String name) throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(name);
			return Converter.convertToEmployeeDTO(employee);
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeByName():查詢員工失敗" + e.getMessage(), e);
		}
	}

	// 根據職位獲取員工資料
	@Override
	public List<EmployeeDTO> selectEmployeeByPosition(String position) throws ServiceException {
		try {
			List<Employee> employees = employeeDaoImpl.findSome(position);
			return employees.stream().map(Converter::convertToEmployeeDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeByPosition():查詢員工失敗" + e.getMessage(), e);
		}
	}

	// 根據部門獲取員工資料
	@Override
	public List<EmployeeDTO> selectEmployeeByDepartment(String department) throws ServiceException {
		try {
			List<Employee> employees = employeeDaoImpl.findSome(department);
			return employees.stream().map(Converter::convertToEmployeeDTO).collect(Collectors.toList());
		} catch (DAOException e) {
			throw new ServiceException("selectEmployeeByDepartment():查詢員工失敗" + e.getMessage(), e);
		}
	}

	// 員工登入
	@Override
	public EmployeeDTO loginEmployee(String username, String password) throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(username);
			if (!(password.equals(employee.getPassword()))) {
				throw new ServiceException("loginEmployee():密碼錯誤");
			}
			return Converter.convertToEmployeeDTO(employee);
		} catch (DAOException e) {
			throw new ServiceException("loginEmployee():帳號錯誤" + e.getMessage(), e);
		}
	}

	//
	@Override
	public boolean checkUsernameBeenUsed(String username) throws ServiceException {
		try {
			return employeeDaoImpl.existsByUsername(username);
		} catch (DAOException e) {
			throw new ServiceException("checkUsernameBeenUsed():檢查用戶名出錯:" + e.getMessage(), e);
		}
	}

	// 更新員工資料
	@Override
	public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeUpdateDTO) throws ServiceException {
		try {
			Employee employee = employeeDaoImpl.findOne(id);
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
			return Converter.convertToEmployeeDTO(employee);
		} catch (DAOException e) {
			throw new ServiceException("updateEmployee():數據庫訪問錯誤: " + e.getMessage(), e);
		}
	}

	// 判別EmployeeUpdateDTO物件內的參數裡面是否為空字串或是null
	private boolean isNotNullOrEmpty(String str) {
		return str != null && !str.isEmpty();
	}

	// 刪除員工資料(僅限chairman)
	@Override
	public void deleteEmployeeById(Integer id) throws ServiceException {
		try {
			employeeDaoImpl.delete(id);
		} catch (DAOException e) {
			throw new ServiceException("deleteOrderById(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}
}
