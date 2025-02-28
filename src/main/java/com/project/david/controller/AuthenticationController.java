package com.project.david.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.david.dto.employee.EmployeeLoginDTO;
import com.project.david.entity.Employee;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class AuthenticationController {
	@Autowired
	EmployeeService employeeService;
	
	@PostMapping("/loginEmployee")
	public ResponseEntity<String> loginEmployee(@RequestBody EmployeeLoginDTO employeeLoginDTO,
								HttpSession session) {
		Employee employee;
		try {
			employee = employeeService.loginEmployee(employeeLoginDTO);
			session.setAttribute("Emp", employee);
			return ResponseEntity.ok("login success. Welcome! "+employee.getName());
		} catch (ServiceException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failure. please try again to login.");
		}
	}
}
