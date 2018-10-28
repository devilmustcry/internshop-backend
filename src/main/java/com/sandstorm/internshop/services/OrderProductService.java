package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.OrderProduct;
import com.sandstorm.internshop.entity.Product;

import java.util.List;

public interface OrderProductService {

    void createOrderProduct(Order order, List<CreateOrderRequest.ProductListRequest> productsOrdered);
}
