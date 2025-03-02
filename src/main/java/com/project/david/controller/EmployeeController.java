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
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	// 1.新增員工(僅限chairman)
	// 檢查用戶名
	// EmployeeDTO 傳送資訊，調用Service的 addEmployee() 註冊員工
	@PostMapping("/addEmployee")
	public ResponseEntity<?> addEmployee(@RequestBody EmployeeDTO employeeDTO,HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (!"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			response.put("message", "You don't have permission to add employee.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}
		try {
			boolean isUsernameUsed = employeeService.checkUsernameBeenUsed(employeeDTO.getUsername());
			if (isUsernameUsed) {
				response.put("message", "add employee failure. username repeat");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			employeeService.addEmployee(employeeDTO);
			response.put("message", "add employee success");
			response.put("name", employeeDTO.getName());
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (ServiceException e) {
			response.put("message", "addEmployee(): fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 2.查詢所有員工(僅限chairman)
	@GetMapping("/allEmployee")
	public ResponseEntity<?> allEmployee(HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (!"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			response.put("message", "You don't have permission to look allEmployees Info.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			List<EmployeeDTO> employees = employeeService.selectAllEmployee();
			return new ResponseEntity<>(employees, HttpStatus.OK);

		} catch (ServiceException e) {
			response.put("message", "allEmployee(): fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 3.根據Id查詢員工
	// 員工只可以查看自己的資料
	// chairman可以查看任意員工的資料
	@GetMapping("/findEmployee/id={id}")
	public ResponseEntity<?> selectEmployeeById(@PathVariable("id") Integer id, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (!currentEmployee.getId().equals(id) && !"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			response.put("message", "You don't have permission to look Employees Info.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			EmployeeDTO employee = employeeService.selectEmployeeById(id);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "findEmployeeId(): fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 4.查看由 name 搜尋的員工資料(chairman)
	@GetMapping("findEmployee/name={name}")
	public ResponseEntity<?> selectEmployeeByName(@PathVariable("name") String name, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		
		if(!"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			response.put("message", "You don't have permission to look Employees Info.");
	        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			EmployeeDTO employee = employeeService.selectEmployeeByName(name);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "findEmployeeName(): fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 5.查看由 position 搜尋的員工資料(chairman)
	@GetMapping("/findEmployee/position={position}")
	public ResponseEntity<?> selectEmployeeByPosition(@PathVariable("position") String position, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (!"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			response.put("message", "You don't have permission to look Employees Info.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			List<EmployeeDTO> employees = employeeService.selectEmployeeByPosition(position);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "findEmployeePosition(): fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 6.chairman可以查看由 department 搜尋的員工資料(chairman)
	@GetMapping("/findEmployee/department={department}")
	public ResponseEntity<?> selectEmployeeByDepartment(@PathVariable("department") String department,
			HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		if (!"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			response.put("message", "You don't have permission to look Employees Info.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			List<EmployeeDTO> employees = employeeService.selectEmployeeByDepartment(department);
			return new ResponseEntity<>(employees, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "findEmployeeDepartment(): fail: " + e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 7.修改員工資料(員工只能修改自己的資料)
	@PutMapping("/updateEmployee/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable("id") Integer id, @RequestBody EmployeeDTO employeeDTO,
			HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		// 只允許本人修改自己的資料
		if (!currentEmployee.getId().equals(id)) {
			response.put("message", "You don't have permission to look updateEmployee.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		try {
			employeeService.updateEmployee(id, employeeDTO);
			response.put("message", "Employee updated successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "updateEmployee(): fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 8.刪除員工資料(chairman)
	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") Integer id, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		EmployeeDTO currentEmployee = (EmployeeDTO) session.getAttribute("Emp");
		if (currentEmployee == null) {
			response.put("message", "please login first.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
		// 只允許 chairman 刪除員工
		if (!"chairman".equalsIgnoreCase(currentEmployee.getPosition())) {
			response.put("message", "You don't have permission to delete employee.");
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}
		
		// 不允許chairman刪除自己
		if(currentEmployee.getId().equals(id)) {
			response.put("message", "You can't delete account yourself.");
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			employeeService.deleteEmployeeById(id);
			response.put("message", "Employee deleted successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ServiceException e) {
			response.put("message", "deleteEmployee(): fail: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
