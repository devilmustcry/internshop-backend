package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.Wrapper.Base.BaseResponse;
import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.Wrapper.Order.CreateOrderResponse;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.services.OrderProductService;
import com.sandstorm.internshop.services.OrderService;
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

    public OrderController(OrderService orderService, OrderProductService orderProductService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        List<CreateOrderRequest.ProductListRequest> productListRequests = orderRequest.getProductListRequestList();

        Order newOrder = orderService.createOrder(orderRequest);
        newOrder = orderProductService.createOrderProduct(newOrder, productListRequests);
        orderService.updateOrderPrice(newOrder.getId(), newOrder);

        BaseResponse<CreateOrderResponse> response = new CreateOrderResponse();
        ((CreateOrderResponse) response).setNetPrice(newOrder.getNetPrice());
        ((CreateOrderResponse) response).setDiscount(newOrder.getDiscount());
        ((CreateOrderResponse) response).setPrice(newOrder.getPrice());
        response.setMessage("Create Order Successfully");
        response.setResponseStatus(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<Order> getOrderByCustomer(@RequestParam(name = "customerId") Long customerId) {
//        Customer customer = customerService.getCustomer(customerId);
        return orderService.getOrderByCustomerId(customerId);

    }
}
