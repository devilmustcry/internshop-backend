package com.sandstorm.internshop.service.OrderProduct;


import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.OrderProduct;
import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.repository.OrderProductRepository;
import com.sandstorm.internshop.service.Order.OrderService;
import com.sandstorm.internshop.service.Product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;

    private final ProductService productService;

    private final OrderService orderService;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository, ProductService productService, OrderService orderService) {
        this.orderProductRepository = orderProductRepository;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    public Order createOrderProducts(Order order, List<CreateOrderRequest.ProductListRequest> productsOrdered) {
        Double discount = 0.0;
        Double price = 0.0;
        for(CreateOrderRequest.ProductListRequest productOrdered : productsOrdered) {
            OrderProduct orderProduct = new OrderProduct();
            Product product = productService.getProduct(productOrdered.getProductId());
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setAmount(productOrdered.getAmount());
            Double productPrice = product.getPrice() * productOrdered.getAmount();
            orderProduct.setNetPrice(price);
            price+=productPrice;
            this.orderProductRepository.save(orderProduct);
        }
        order.setPrice(price);
        Long orderCount = orderService.countOrderByCustomerId(order.getCustomer().getId());
        if (orderCount.longValue() >= OrderProductServiceConstant.DISCOUNT_THRESHOLD) {
            discount = price * OrderProductServiceConstant.DISCOUNT_PERCENT;
        }
        order.setDiscount(discount);
        order.setNetPrice(price - discount);
        return order;
    }

    private static class OrderProductServiceConstant {
        private static final Long DISCOUNT_THRESHOLD = 4L;
        private static final Double DISCOUNT_PERCENT = 0.1;
    }
}
