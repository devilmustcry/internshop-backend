package com.sandstorm.internshop.service.coupon.strategy;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;

public class FixRateDiscountStrategy implements DiscountStrategy {

    @Override
    public Order applyDiscount(Order order, Coupon coupon) {
        order.setDiscount(order.getDiscount() + coupon.getDiscount())
                .setNetPrice(order.getNetPrice() - coupon.getDiscount());
        return order;
    }
}
