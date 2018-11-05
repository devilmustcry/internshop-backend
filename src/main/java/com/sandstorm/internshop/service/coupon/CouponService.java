package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;

public interface CouponService {

    Coupon createCoupon(Coupon coupon);

    Coupon getCouponByCode(String code);

    Coupon useCoupon(Long id);

    Order applyCoupon(Order order, Coupon coupon);
}
