package com.sandstorm.internshop.services;


import com.sandstorm.internshop.Wrapper.CreateOrderRequest;
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
    public void createOrderProduct(Order order, List<CreateOrderRequest.ProductListRequest> productsOrdered) {
        for(CreateOrderRequest.ProductListRequest productOrdered : productsOrdered) {
            OrderProduct orderProduct = new OrderProduct();
            Product product = productService.getProduct(productOrdered.getProductId());
//            product.setId(productOrdered.getProductId());
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setAmount(productOrdered.getAmount());
            orderProduct.setNetPrice(product.getPrice() * productOrdered.getAmount());
            this.orderProductRepository.save(orderProduct);
        }
    }
}
