package com.project.david.dao.impl.jpa;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.david.entity.Order;

/*
 * 	create -> save()	
 * 	findOne() -> findById()
 * 	findSome() -> findByOrderDate(), findByOrderDateBetween()
 * 	findAll() -> findAll()
 * 	update() -> save()
 * 	delete() -> deleteById()
 */

public interface OrderRepository extends JpaRepository<Order,Integer>{
	List<Order> findByOrderDate(LocalDate orderDate);
	List<Order> findByOrderDateBetween(LocalDate startDate,LocalDate endDate);
	boolean existsByOrderDate(LocalDate orderDate);
}
