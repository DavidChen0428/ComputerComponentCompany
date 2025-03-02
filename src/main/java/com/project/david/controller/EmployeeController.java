package com.project.david.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.david.dto.EmployeeDTO;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;

import jakarta.servlet.http.HttpSession;

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
@RequestMapping("/employeeAPI")
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;

	// 註冊新員工(新增)
	@PostMapping("/addEmployee")
	public ResponseEntity<Map<String, String>> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			if (!employeeService.checkUsernameBeenUsed(employeeDTO.getUsername())) {
				employeeService.addEmployee(employeeDTO);
				response.put("message", "add employee success");
				response.put("name", employeeDTO.getName());
				response.put("username", employeeDTO.getUsername());
				response.put("position", employeeDTO.getPosition());
				response.put("department", employeeDTO.getDepartment());
				return new ResponseEntity<>(response, HttpStatus.CREATED);
			} else {
				response.put("message", "add employee failure. username repeat");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (ServiceException e) {
			response.put("message", "Employee addition failed: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 查詢所有員工(僅限chairman)
	@GetMapping("/allEmployee")
	public ResponseEntity<List<EmployeeDTO>> allEmployee(HttpSession session) {
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}

		try {
			if ("chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
				List<EmployeeDTO> employees = employeeService.selectAllEmployee();
				return new ResponseEntity<>(employees, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 員工可以查看自己的資料，或是職位是chairman可以查看任意員工的資料
	@GetMapping("/findEmployee/id={id}")
	public ResponseEntity<EmployeeDTO> selectEmployeeById(@PathVariable("id") Integer id, HttpSession session) {
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}

		try {
			if (currentEmployee.getId().equals(id) || "chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
				EmployeeDTO employee = employeeService.selectEmployeeById(id);
				return new ResponseEntity<>(employee, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// chairman可以查看由 name 搜尋的員工資料
	@GetMapping("findEmployee/name={name}")
	public ResponseEntity<EmployeeDTO> selectEmployeeByName(@PathVariable("name") String name, HttpSession session) {
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null || !"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		try {
			EmployeeDTO employee = employeeService.selectEmployeeByName(name);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// chairman可以查看由 position 搜尋的員工資料
	@GetMapping("/findEmployee/position={position}")
	public ResponseEntity<List<EmployeeDTO>> selectEmployeeByPosition(@PathVariable("position") String position,
			HttpSession session) {
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null || !"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		try {
			List<EmployeeDTO> employees = employeeService.selectEmployeeByPosition(position);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// chairman可以查看由 department 搜尋的員工資料
	@GetMapping("/findEmployee/department={department}")
	public ResponseEntity<List<EmployeeDTO>> selectEmployeeByDepartment(@PathVariable("department") String department,
			HttpSession session) {
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null || !"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		try {
			List<EmployeeDTO> employees = employeeService.selectEmployeeByDepartment(department);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ServiceException e) {
			// Log the exception (optional, for debugging purposes)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 員工只能修改自己的資料
	// chairman可以修改任意員工的資料
	@PutMapping("/updateEmployee/{id}")
	public ResponseEntity<Map<String, String>> updateEmployee(@PathVariable("id") Integer id,
			@RequestBody EmployeeDTO employeeDTO, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}

		try {
			if (currentEmployee.getId().equals(id) || "chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
				employeeService.updateEmployee(id, employeeDTO);
				response.put("message", "Employee updated successfully.");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}
		} catch (ServiceException e) {
			response.put("message", "Employee update failed: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 刪除員工資料(僅限於chairman)
	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable("id") Integer id, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		try {
			if ("chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
				employeeService.deleteEmployeeById(id);
				response.put("message", "Employee deleted successfully.");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}

		} catch (ServiceException e) {
			response.put("message", "Employee deleted failed: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
