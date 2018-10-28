package com.sandstorm.internshop.services;


import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.OrderProduct;
import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.repository.OrderProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;

    private final ProductService productService;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository, ProductService productService) {
        this.orderProductRepository = orderProductRepository;
        this.productService = productService;
    }

    @Override
    public Double createOrderProduct(Order order, List<CreateOrderRequest.ProductListRequest> productsOrdered) {
        Double netPrice = 0.0;
        for(CreateOrderRequest.ProductListRequest productOrdered : productsOrdered) {
            OrderProduct orderProduct = new OrderProduct();
            Product product = productService.getProduct(productOrdered.getProductId());
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setAmount(productOrdered.getAmount());
            Double price = product.getPrice() * productOrdered.getAmount();
            orderProduct.setNetPrice(price);
            netPrice+=price;
            this.orderProductRepository.save(orderProduct);
        }
        return netPrice;
    }
}
