package com.project.david.dao;

import java.util.List;

// DAO架構
/*
 * 	C -> 創建 create
 * 	R -> 查詢(單一findOne、多個findSome、全部findAll)
 * 	U -> 修改(灌注方式)update
 * 	D -> 刪除(目前使用Id刪除)delete
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
