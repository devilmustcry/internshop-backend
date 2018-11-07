package com.sandstorm.internshop.service.coupon.strategy;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;

public class PriceCouponStrategy extends CouponStrategy {


    public PriceCouponStrategy(DiscountStrategy discountStrategy) {
        super(discountStrategy);
    }

    @Override
    public Order discount(Order order, Coupon coupon) {
        if (order.getNetPrice() >= coupon.getPriceThreshold()) {
            order = super.getDiscountStrategy().applyDiscount(order, coupon);
            order.setIsUsedCoupon(true);
        }
        return order;
    }



}
