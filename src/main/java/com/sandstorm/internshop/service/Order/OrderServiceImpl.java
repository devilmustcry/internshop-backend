package com.sandstorm.internshop.service.Order;

import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.exception.OrderNotFound;
import com.sandstorm.internshop.repository.OrderRepository;
import com.sandstorm.internshop.service.Customer.CustomerService;
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
    public Order updateOrderPrice(Long id, Order order) {
        return orderRepository.findById(order.getId()).map((it) -> {
            it.setNetPrice(order.getNetPrice());
            it.setPrice(order.getPrice());
            it.setDiscount(order.getDiscount());
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
