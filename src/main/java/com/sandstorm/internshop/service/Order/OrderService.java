package com.sandstorm.internshop.service.Order;

import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderRequest newOrder);

    Order updateOrderPrice(Long id, Order order);

    List<Order> getOrderByCustomerId(Long customerId);

    Long countOrderByCustomerId(Long customerId);
}
