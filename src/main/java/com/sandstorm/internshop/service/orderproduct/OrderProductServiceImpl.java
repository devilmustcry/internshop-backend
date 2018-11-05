package com.sandstorm.internshop.service.orderproduct;


import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.entity.product.OrderProduct;
import com.sandstorm.internshop.entity.product.Product;
import com.sandstorm.internshop.repository.product.OrderProductRepository;
import com.sandstorm.internshop.service.order.OrderService;
import com.sandstorm.internshop.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderProductServiceImpl implements OrderProductService {

    private static final Long DISCOUNT_THRESHOLD = 4L;
    private static final Double DISCOUNT_PERCENT = 0.1;

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
        if (orderCount.longValue() >= DISCOUNT_THRESHOLD) {
            discount = price * DISCOUNT_PERCENT;
        }
        order.setDiscount(discount);
        order.setNetPrice(price - discount);
        return order;
    }

    @Override
    public Long countProductInOrder(Order orderId) {
        Long count = orderProductRepository.countByOrder(orderId);
        log.info(count.toString());
        return count;
    }


}
