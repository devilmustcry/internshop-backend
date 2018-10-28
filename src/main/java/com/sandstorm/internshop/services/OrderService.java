package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderRequest newOrder);

    List<Order> getOrderByCustomerId(Long customerId);
}
