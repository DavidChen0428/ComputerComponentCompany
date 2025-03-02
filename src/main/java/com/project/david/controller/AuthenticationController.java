package com.project.david.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.david.dto.employee.EmployeeDTO;
import com.project.david.dto.employee.LoginRequest;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class AuthenticationController {
	@Autowired
	EmployeeService employeeService;
	
	@PostMapping("/loginEmployee")
	public ResponseEntity<Map<String,String>> loginEmployee(@RequestBody LoginRequest loginRequest,
								HttpSession session) {
		Map<String,String> response=new HashMap<>();
		try {
			EmployeeDTO loggedInEmployee = employeeService.loginEmployee(loginRequest.getUsername(),loginRequest.getPassword());
			session.setAttribute("Emp", loggedInEmployee);
			response.put("message", "Login successfully.");
			response.put("name", loggedInEmployee.getName());
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("error", "Login failed: "+e.getMessage());
			return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
		}
	}
}
