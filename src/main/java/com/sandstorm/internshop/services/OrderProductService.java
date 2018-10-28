package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;

import java.util.List;

public interface OrderProductService {

    Double createOrderProduct(Order order, List<CreateOrderRequest.ProductListRequest> productsOrdered);
}
