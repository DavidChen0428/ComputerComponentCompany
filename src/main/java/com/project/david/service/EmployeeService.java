package com.project.david.service;

import java.util.List;

import com.project.david.dto.EmployeeDTO;

/*
 * 公司員工管理功能 :(權限人資或主管)
 * addEmployee -> 新增員工create()
 * 
 * selectAllEmployee -> 搜尋所有員工findAll()
 * selectEmployeeById -> 用員工編號作搜尋(單筆結果)findOne()
 * selectEmployeeByPosition -> 用職位作搜尋(多筆結果)findSome()
 * selectEmployeeByDepartment -> 用部門作搜尋(多筆結果)findSome()
 * selectEmployeeByName -> 用姓名來搜尋(假定姓名不會重複)findOne()
 * 
 * username、password是用來登入的
 * loginEmployee -> 作登入使用findOne()
 * checkUsernameBeenUsed -> 新增員工時，帳號不能相同existsByUsername()
 * 
 * updateEmployee -> 修改帳號以外的東西，密碼、姓名、職位、部門(帳號目前被限制無法被更改)findOne(), update()
 * 
 * deleteEmployee -> 刪除員工資料(需輸入該員工資料的帳號密碼)findOne(), delete()
 */
public interface EmployeeService {
	// create
	EmployeeDTO addEmployee(EmployeeDTO employeeDTO) throws ServiceException;
	
	// read
	List<EmployeeDTO> selectAllEmployee() throws ServiceException;
	EmployeeDTO selectEmployeeById(Integer id) throws ServiceException;
	EmployeeDTO selectEmployeeByName(String name) throws ServiceException;
	List<EmployeeDTO> selectEmployeeByPosition(String position) throws ServiceException;
	List<EmployeeDTO> selectEmployeeByDepartment(String department) throws ServiceException;
	
	// login
	EmployeeDTO loginEmployee(String username, String password) throws ServiceException;
	boolean checkUsernameBeenUsed(String username) throws ServiceException;
	
	// update
	EmployeeDTO updateEmployee(Integer id,EmployeeDTO employeeDTO) throws ServiceException;
	
	// delete
	void deleteEmployeeById(Integer id) throws ServiceException;
	
	
}
