package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderByCustomerId(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

}
