package com.sandstorm.internshop.service.coupon.strategy;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;

public class PercentageDiscountStrategy implements DiscountStrategy {

    private final Integer PERCENTAGE = 100;

    @Override
    public Order applyDiscount(Order order, Coupon coupon) {
        Double discount = getDiscount(coupon.getDiscount().intValue(), order.getNetPrice());
        order.setDiscount(order.getDiscount() + discount)
                .setNetPrice(order.getNetPrice() - discount);
        return order;
    }

    private Double getDiscount(Integer percent, Double netPrice) {
        return netPrice*percent/PERCENTAGE;
    }
}
