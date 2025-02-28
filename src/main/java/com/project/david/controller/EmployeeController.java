package com.project.david.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.david.dto.employee.EmployeeRegisterDTO;
import com.project.david.entity.Employee;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;

/*
 * 	員工管理系統該要有的方法 :
 * 	1.新增員工資料
 * 	2.查詢所有員工資料
 * 	3.利用員工編號查詢符合的員工資料
 * 	4.利用職位查詢符合的員工資料
 * 	5.利用部門來查詢符合的員工資料
 * 	6.利用姓名來查詢符合的員工資料
 * 	7.修改員工的基本資料
 * 	8.刪除員工資料
 */

@RestController
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;

	@PostMapping("addEmployee")
	public ResponseEntity<Map<String, String>> addEmployee(@RequestBody EmployeeRegisterDTO employeeRegisterDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			employeeService.addEmployee(employeeRegisterDTO);
			response.put("message", "add employee success");
			response.put("name", employeeRegisterDTO.getName());
			response.put("username", employeeRegisterDTO.getUsername());
			response.put("password", employeeRegisterDTO.getPassword());
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (ServiceException e) {
			response.put("message", "add employee failure. please check whether your profile correct or not.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("allEmployee")
	public ResponseEntity<List<Employee>> allEmployee() {
		try {
			List<Employee> employees = employeeService.selectAllEmployee();
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("findEmployee/id/{id}")
	public ResponseEntity<Employee> selectEmployeeById(@PathVariable("id") Integer id) {
		try {
			Employee employee = employeeService.selectEmployeeById(id);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("findEmployee/name/{name}")
	public ResponseEntity<Employee> selectEmployeeByName(@PathVariable("name") String name) {
		try {
			Employee employee = employeeService.selectEmployeeByName(name);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("findEmployee/position/{position}")
	public ResponseEntity<List<Employee>> selectEmployeeByPosition(@PathVariable("position") String position) {
		try {
			List<Employee> employees = employeeService.selectEmployeeByPosition(position);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("findEmployee/department/{department}")
	public ResponseEntity<List<Employee>> selectEmployeeByDepartment(@PathVariable("department") String department) {
		try {
			List<Employee> employees = employeeService.selectEmployeeByDepartment(department);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

}
