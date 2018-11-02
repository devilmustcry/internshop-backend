package com.sandstorm.internshop.service.orderproduct;

import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.product.Order;

import java.util.List;

public interface OrderProductService {

    Order createOrderProducts(Order order, List<CreateOrderRequest.ProductListRequest> productsOrdered);
}
