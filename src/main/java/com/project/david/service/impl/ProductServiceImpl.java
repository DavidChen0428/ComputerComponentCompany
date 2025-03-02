package com.project.david.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.david.dao.DAOException;
import com.project.david.dao.impl.jpa.ProductDaoImpl;
import com.project.david.dto.Converter;
import com.project.david.dto.ProductDTO;
import com.project.david.dto.ConvertException;
import com.project.david.entity.Order;
import com.project.david.entity.Product;
import com.project.david.service.ProductService;
import com.project.david.service.ServiceException;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDaoImpl productDaoImpl;

	@Override
	public void addProduct(ProductDTO productDTO) throws ServiceException {
		try {
			Product product = Converter.convertToProduct(productDTO);
			productDaoImpl.create(product);
		} catch (DAOException e) {
			throw new ServiceException("addProduct(): 資料庫訪問層錯誤: " + e.getMessage(), e);
		} catch (ConvertException e) {
			throw new ServiceException("addProduct(): 資料型態轉換錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Product> selectAllProduct() throws ServiceException {
		try {
			return productDaoImpl.findAll();
		} catch (DAOException e) {
			throw new ServiceException("selectAllProduct():資料庫訪問層錯誤: " + e.getMessage(), e);
		}
	}

	@Override
	public Product selectProductById(Integer id) throws ServiceException {
		try {
			return productDaoImpl.findOne(id);
		} catch (DAOException e) {
			throw new ServiceException("selectProductById(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

	@Override
	public List<Product> selectProductByName(String name) throws ServiceException {
		try {
			return productDaoImpl.findSome(name);
		} catch (DAOException e) {
			throw new ServiceException("selectProductByName(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}

	@Override
	public void updateProduct(ProductDTO productDTO) throws ServiceException {
		try {
			Product product = productDaoImpl.findOne(productDTO.getId());
			product.setName(productDTO.getName());
			product.setPrice(productDTO.getPrice());
			product.setQuantity(productDTO.getQuantity());
			if (productDTO.getOrderId() != null) {
				Order order = new Order();
				order.setId(productDTO.getOrderId());
				product.setOrder(order);
			} else {
				product.setOrder(null);
			}
			productDaoImpl.update(product);
		} catch (DAOException e) {
			throw new ServiceException("updateProduct(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}

	}

	@Override
	public void deleteProductById(Integer id) throws ServiceException {
		try {
			productDaoImpl.delete(id);
		}catch(DAOException e) {
			throw new ServiceException("deleteProduuctById(): 資料庫訪問層錯誤" + e.getMessage(), e);
		}
	}
}
