package com.sandstorm.internshop.repository;

import com.sandstorm.internshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
