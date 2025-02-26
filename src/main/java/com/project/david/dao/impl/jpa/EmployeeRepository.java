package com.project.david.dao.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.david.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{

}
