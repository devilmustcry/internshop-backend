package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.exception.CouponNotFound;
import com.sandstorm.internshop.security.CurrentUser;
import com.sandstorm.internshop.service.coupon.CouponService;
import com.sandstorm.internshop.wrapper.Base.BaseResponse;
import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.wrapper.Order.CreateOrderResponse;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.service.orderproduct.OrderProductService;
import com.sandstorm.internshop.service.order.OrderService;
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

    private final CouponService couponService;

    public OrderController(OrderService orderService, OrderProductService orderProductService, CouponService couponService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.couponService = couponService;
    }


    @PostMapping
    public ResponseEntity<BaseResponse<CreateOrderResponse>> createOrder(@CurrentUser Long customerId, @RequestBody CreateOrderRequest orderRequest) {
        List<CreateOrderRequest.ProductListRequest> productListRequests = orderRequest.getProductListRequestList();
        Order newOrder = orderService.createOrder(customerId, orderRequest);
        newOrder = orderProductService.createOrderProducts(newOrder, productListRequests);
        String couponResult = "";
        if (orderRequest.hasCoupon()) {
            try {
                Coupon coupon = couponService.getCouponByCode(orderRequest.getCouponCode());
                newOrder = couponService.applyCoupon(newOrder, coupon);
            } catch (CouponNotFound e) {
                couponResult = e.getMessage();
            }
        }
        orderService.updateOrderPrice(newOrder.getId(), newOrder);

        CreateOrderResponse response = new CreateOrderResponse();
        response.setNetPrice(newOrder.getNetPrice())
                .setDiscount(newOrder.getDiscount())
                .setPrice(newOrder.getPrice())
                .setIsUsedCoupon(newOrder.getIsUsedCoupon())
                .setCouponResult(couponResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(HttpStatus.CREATED, "Create order successfully", response));
    }

    @GetMapping
    public BaseResponse<List<Order>> getOrderByCustomer(@CurrentUser Long customerId) {
        return new BaseResponse<List<Order>>(HttpStatus.OK, "Get order By customer", orderService.getOrderByCustomerId(customerId));

    }
}
