package com.project.david.dao;

import java.util.List;

// DAO的範本，通用於每個ORM
/*
 * C -> 創建
 * R -> 查詢(單一、多個、全部)
 * U -> 修改(灌注方式)
 * D -> 刪除(可以key類型不同刪除，不一定要用ID)
 */
public interface BaseDAO<T> {
	
	// create
	void create(T data) throws DAOException;
	
	// read
	T findOne(Object key) throws DAOException;
	List<T> findAll() throws DAOException;
	List<T> findSome(Object key) throws DAOException;
	
	// update
	void update(T data) throws DAOException;
	
	// delete
	void delete(Object key) throws DAOException;
}
