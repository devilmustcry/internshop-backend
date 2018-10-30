package com.sandstorm.internshop.service.OrderProduct;

import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;

import java.util.List;

public interface OrderProductService {

    Order createOrderProducts(Order order, List<CreateOrderRequest.ProductListRequest> productsOrdered);
}
