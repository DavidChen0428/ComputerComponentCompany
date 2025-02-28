package com.project.david;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.david.entity.Employee;
import com.project.david.service.EmployeeService;
import com.project.david.service.ServiceException;


// 20250227_Test_Success
@SpringBootTest
public class EmployeeServiceImplTest {
	@Autowired
	EmployeeService employeeService;
	
	@Test
	public void test() throws ServiceException{
		
		// 1.
		//employeeService.addEmployee(new Employee("RubyWu","ruby","1234","staff","PD"));
		//System.out.println("add employee success");
		
		// 2.
		List<Employee> employees=employeeService.selectAllEmployee();
		for(Employee employee:employees) {
			System.out.printf("%d) %s, %s, %s\n",employee.getId(),employee.getName(),employee.getDepartment(),employee.getPosition());
		}
		
		// 3.
		//Employee employee=employeeService.selectEmployeeById(3);
		//System.out.printf("%d) %s, %s, %s\n",employee.getId(),employee.getName(),employee.getDepartment(),employee.getPosition());
	
		// 4.
		//Employee employee=employeeService.selectEmployeeByName("FrankChung");
		//System.out.printf("%d) %s, %s, %s\n",employee.getId(),employee.getName(),employee.getDepartment(),employee.getPosition());
	
		// 5.
		//List<Employee> employees=employeeService.selectEmployeeByPosition("manager");
		//for(Employee employee: employees) {
		//	System.out.printf("%d) %s, %s, %s\n",employee.getId(),employee.getName(),employee.getDepartment(),employee.getPosition());	
		//}
		
		// 6.
		//Employee employee=employeeService.loginEmployee("frank", "1234");
		//System.out.println(employee==null);
	
		// 7.
		//employeeService.updateEmployee(18,"","","leader","");
		//System.out.println("update employee success");
		
		// 8.
		//employeeService.deleteEmployee("LindaTsai","1234");
		//System.out.println("delete employee success");
		
		
	}
}
