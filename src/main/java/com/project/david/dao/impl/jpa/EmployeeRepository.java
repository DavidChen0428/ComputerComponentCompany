package com.project.david.dao.impl.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.david.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
	/*	
	 * 	create() -> save()
	 * 	findOne() -> findByName, findByUsername(), findById(), existsByName(), existsByUsername() 
	 * 	findSome() -> findByPosition(), findByDepartment(), existsByPosition(), existsByDepartment() 
	 * 	findAll() -> findAll()
	 * 	update() -> save()
	 * 	delete() -> deleteById()
	 */
	
	List<Employee> findByName(String name);
	List<Employee> findByPosition(String position);
	List<Employee> findByDepartment(String department);
	List<Employee> findByUsername(String username);
	boolean existsByPosition(String position);
	boolean existsByDepartment(String department);
	boolean existsByName(String name);
	boolean existsByUsername(String username);
}
