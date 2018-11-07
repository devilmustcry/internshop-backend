package com.sandstorm.internshop.service.coupon.strategy;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;
import lombok.Getter;

@Getter
public abstract class CouponStrategy {

    private DiscountStrategy discountStrategy;

    public CouponStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public abstract Order discount(Order order, Coupon coupon);

}
