package com.project.david.dao.impl.jpa;

import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionException;

import com.project.david.dao.BaseDAO;
import com.project.david.dao.DAOException;
import com.project.david.entity.Product;

@Repository
public class ProductDaoImpl implements BaseDAO<Product> {
	@Autowired
	ProductRepository productRepository;

	@Override
	public void create(Product data) throws DAOException {
		try {
			productRepository.save(data);
		} catch (DataIntegrityViolationException e) {
			throw new DAOException("create():數據違反約束條件: " + e.getMessage(), e);
		} catch (ConstraintViolationException e) {
			throw new DAOException("create():數據違反約束條件: " + e.getMessage(), e);
		} catch (TransactionException e) {
			throw new DAOException("create():事務操作失敗: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new DAOException("create():非法參數: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("create():未知錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public Product findOne(Object key) throws DAOException {
		if (key instanceof Integer keyInt) {
			Optional<Product> optionalProduct = productRepository.findById(keyInt);
			if (optionalProduct.isPresent()) {
				return optionalProduct.get();
			} else {
				throw new DAOException("findOne():沒有符合條件的產品" + key.toString());
			}
		}
		throw new DAOException("findOne():無效的key參數類型: " + key.getClass().getName());
	}

	@Override
	public List<Product> findAll() throws DAOException {
		try {
			return productRepository.findAll();
		} catch (DataAccessException e) {
			throw new DAOException("findAll():數據訪問失敗: " + e.getMessage(), e);
		} catch (TransactionException e) {
			throw new DAOException("findAll():事務操作失敗: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new DAOException("findAll():非法參數: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("findAll():未知錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Product> findSome(Object key) throws DAOException {
		if (key instanceof String keyStr) {
			try {
				if(productRepository.existsByName(keyStr)) {
					return productRepository.findByName(keyStr);
				}
				throw new DAOException("findSome():沒有符合商品名稱的訂單" + keyStr);
			} catch (DataAccessException e) {
				throw new DAOException("findSome(): 數據訪問失敗: " + e.getMessage(), e);
			} catch (Exception e) {
				throw new DAOException("findSome(): 未知錯誤: " + e.getMessage(), e);
			}
		}
		throw new DAOException("findSome(): 無效的 key 參數類型: " + key.getClass().getName());
	}

	@Override
	public void update(Product data) throws DAOException {
		try {
			if (data.getId() == null) {
				throw new DAOException("update():產品ID不能為空");
			}
			productRepository.save(data);
		} catch (DataIntegrityViolationException e) {
			throw new DAOException("update():數據違反約束條件: " + e.getMessage(), e);
		} catch (ConstraintViolationException e) {
			throw new DAOException("update():數據違反約束條件: " + e.getMessage(), e);
		} catch (TransactionException e) {
			throw new DAOException("update():事務操作失敗: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new DAOException("update():非法參數: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("update():未知錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public void delete(Object key) throws DAOException {
		try {
			if (key instanceof Integer) {
				productRepository.deleteById((Integer) key);
			} else {
				throw new DAOException("delete():無效的key參數類型: " + key.getClass().getName());
			}
		} catch (EmptyResultDataAccessException e) {
			throw new DAOException("delete():刪除失敗，沒有找到對應的記錄: " + key.toString(), e);
		} catch (DataAccessException e) {
			throw new DAOException("delete():數據訪問失敗: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DAOException("delete():未知錯誤: " + e.getMessage(), e);
		}
	}
}
