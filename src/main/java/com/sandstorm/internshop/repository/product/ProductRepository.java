package com.sandstorm.internshop.repository.product;

import com.sandstorm.internshop.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(List<Long> idList);
}
