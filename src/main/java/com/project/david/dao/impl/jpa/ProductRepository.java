package com.project.david.dao.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.david.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{

}
