package com.sandstorm.internshop.service.order;

import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.product.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Long customerId, CreateOrderRequest newOrder);

    Order updateOrderPrice(Long id, Order order);

    List<Order> getOrderByCustomerId(Long customerId);

    Long countOrderByCustomerId(Long customerId);
}
