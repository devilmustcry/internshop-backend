package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.OrderProduct;
import com.sandstorm.internshop.exception.OrderNotFound;
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
    public void updateOrderPrice(Long id, Order order) {
        orderRepository.findById(order.getId()).map((it) -> {
            it.setNetPrice(order.getNetPrice());
            return orderRepository.save(it);
        }).orElseThrow(() -> new OrderNotFound("Cannot find order with id " + order.getId()));
    }

    @Override
    public List<Order> getOrderByCustomerId(Long customerId)
    {
        return orderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public Long countOrderByCustomerId(Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return orderRepository.countByCustomer(customer);
    }


}
