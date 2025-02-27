package com.project.david.service;

import java.util.List;

import com.project.david.entity.Employee;

/*
 * 公司員工管理 :(權限人資或主管)
 * addEmployee -> 新增員工
 * 
 * selectAllEmployee -> 搜尋所有員工
 * selectEmployeeById -> 用員工編號作搜尋(單筆結果)
 * selectEmployeeByPosition -> 用職位作搜尋(多筆結果)
 * selectEmployeeByDepartment -> 用部門作搜尋(多筆結果)
 * selectEmployeeByName -> 用姓名來搜尋(假定姓名不會重複)
 * 
 * username、password是用來登入的
 * loginEmployee -> 作登入使用
 * checkUsernameBeenUsed -> 新增員工時，帳號不能相同
 * 
 * updateEmployee -> 修改帳號以外的東西，密碼、姓名、職位、部門(帳號目前被限制無法被更改)
 * 
 * deleteEmployee -> 刪除員工資料(需輸入該員工資料的帳號密碼)
 */
public interface EmployeeService {
	// create
	void addEmployee(Employee employee) throws ServiceException;
	
	// read
	List<Employee> selectAllEmployee() throws ServiceException;
	Employee selectEmployeeById(Integer id) throws ServiceException;
	Employee selectEmployeeByName(String name) throws ServiceException;
	List<Employee> selectEmployeeByPosition(String position) throws ServiceException;
	List<Employee> selectEmployeeByDepartment(String department) throws ServiceException;
	
	// login
	Employee loginEmployee(String username,String password) throws ServiceException;
	boolean checkUsernameBeenUsed(String username) throws ServiceException;
	
	// update
	void updateEmployee(Integer id, String name, String password, String position, String department) throws ServiceException;
	
	// delete
	void deleteEmployee(String username,String password) throws ServiceException;
	
	
}
