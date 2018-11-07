package com.sandstorm.internshop.repository.product;

import com.sandstorm.internshop.entity.product.Customer;
import com.sandstorm.internshop.entity.product.Order;
import org.hibernate.annotations.LazyCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(Long customerId);

    Long countByCustomer(Customer id);

}
