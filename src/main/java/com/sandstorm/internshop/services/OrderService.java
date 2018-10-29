package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderRequest newOrder);

    void updateOrderPrice(Long id, Order order);

    List<Order> getOrderByCustomerId(Long customerId);

    Long countOrderByCustomerId(Long customerId);
}
