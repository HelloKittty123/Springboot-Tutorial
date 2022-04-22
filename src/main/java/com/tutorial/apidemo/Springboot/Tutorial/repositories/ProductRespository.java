package com.tutorial.apidemo.Springboot.Tutorial.repositories;

import com.tutorial.apidemo.Springboot.Tutorial.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRespository extends JpaRepository<Product,Long > {
    List<Product> findByProductName(String productName);
}
