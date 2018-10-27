package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.services.CustomerService;
import com.sandstorm.internshop.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<Order> createOrder(@PathVariable(name = "customerId") Long customerId, Order order) {
        Customer customer = customerService.getCustomer(customerId);
        order.setCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    @GetMapping
    public List<Order> getOrderByCustomer(@RequestParam(name = "customerId") Long customerId) {
//        Customer customer = customerService.getCustomer(customerId);
        return orderService.getOrderByCustomerId(customerId);

    }
}
