package com.project.david.dto.employee;

import com.project.david.entity.Employee;

public class EmployeeConverter {
	// Employee 類的 DTO 轉換器
	// Employee -> DTO 通常是要資料送出到客戶顯示
	// DTO -> Employee 新增
	// DTO -> 資料提取
	
	// Employee -> EmployeeRegisterDTO
	public static EmployeeRegisterDTO convertToRegisterDTO(Employee employee) {
		EmployeeRegisterDTO dto=new EmployeeRegisterDTO();
		dto.setName(employee.getName());
		dto.setUsername(employee.getUsername());
		dto.setPassword(employee.getPassword());
		dto.setPosition(employee.getPosition());
		dto.setDepartment(employee.getDepartment());
		return dto;
	}
	
	// EmployeeRegisterDTO -> Employee
	public static Employee convertToEmployeeByRegisterDTO(EmployeeRegisterDTO employeeRegisterDTO) {
		Employee employee=new Employee();
		employee.setName(employeeRegisterDTO.getName());
		employee.setUsername(employeeRegisterDTO.getUsername());
		employee.setPassword(employeeRegisterDTO.getPassword());
		employee.setPosition(employeeRegisterDTO.getPosition());
		employee.setDepartment(employeeRegisterDTO.getDepartment());
		return employee;
	}
	
}
