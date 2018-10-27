package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> getOrderByCustomerId(Long customerId);
}
