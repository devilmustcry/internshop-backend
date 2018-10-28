package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.Wrapper.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.services.CustomerService;
import com.sandstorm.internshop.services.OrderProductService;
import com.sandstorm.internshop.services.OrderService;
import com.sandstorm.internshop.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final OrderProductService orderProductService;

    private final ProductService productService;

    public OrderController(OrderService orderService, OrderProductService orderProductService, ProductService productService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        List<CreateOrderRequest.ProductListRequest> productListRequests = orderRequest.getProductListRequestList();

        Order newOrder = orderService.createOrder(orderRequest);
        orderProductService.createOrderProduct(newOrder, productListRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping
    public List<Order> getOrderByCustomer(@RequestParam(name = "customerId") Long customerId) {
//        Customer customer = customerService.getCustomer(customerId);
        return orderService.getOrderByCustomerId(customerId);

    }

    static class ProductList extends ArrayList<Product> {

    }
}
