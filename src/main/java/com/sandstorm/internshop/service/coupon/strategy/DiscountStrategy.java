package com.sandstorm.internshop.service.coupon.strategy;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;

public interface DiscountStrategy {

    Order applyDiscount(Order order, Coupon coupon);
}
