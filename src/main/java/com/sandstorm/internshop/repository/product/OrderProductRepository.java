package com.sandstorm.internshop.repository.product;

import com.sandstorm.internshop.entity.product.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
