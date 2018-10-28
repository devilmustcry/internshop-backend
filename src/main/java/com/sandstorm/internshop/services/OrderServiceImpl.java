package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    private final CustomerService customerService;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @Override
    public Order createOrder(CreateOrderRequest newOrder) {
        Order order = new Order();
        order.setCustomer(customerService.getCustomer(newOrder.getCustomerId()));
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderByCustomerId(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

}
