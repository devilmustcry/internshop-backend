package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.security.CurrentUser;
import com.sandstorm.internshop.wrapper.Base.BaseResponse;
import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.wrapper.Order.CreateOrderResponse;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.service.OrderProduct.OrderProductService;
import com.sandstorm.internshop.service.Order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BaseResponse<CreateOrderResponse>> createOrder(@CurrentUser Long customerId, @RequestBody CreateOrderRequest orderRequest) {
        List<CreateOrderRequest.ProductListRequest> productListRequests = orderRequest.getProductListRequestList();

        Order newOrder = orderService.createOrder(customerId, orderRequest);
        newOrder = orderProductService.createOrderProducts(newOrder, productListRequests);
        orderService.updateOrderPrice(newOrder.getId(), newOrder);

        CreateOrderResponse response = new CreateOrderResponse();
        response.setNetPrice(newOrder.getNetPrice())
                .setDiscount(newOrder.getDiscount())
                .setPrice(newOrder.getPrice());

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(HttpStatus.CREATED, "Create order successfully", response));
    }

    @GetMapping
    public BaseResponse<List<Order>> getOrderByCustomer(@CurrentUser Long customerId) {
        return new BaseResponse<List<Order>>(HttpStatus.OK, "Get Order By Customer", orderService.getOrderByCustomerId(customerId));

    }
}
