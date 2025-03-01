package com.project.david.dao.impl.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.david.entity.Product;

/*
 * 	create -> save()
 * 	findOne() -> findById()
 * 	findSome() -> findByName(),existsByName()
 * 	findAll() -> findAll()
 * 	update() -> save()
 * 	delete() -> deleteById()
 */


public interface ProductRepository extends JpaRepository<Product,Integer>{
	List<Product> findByName(String name);
	boolean existsByName(String name);
}
