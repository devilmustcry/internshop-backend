package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;

public interface CouponService {

    Coupon createCoupon(Coupon coupon);

    Coupon getCouponByCode(String code);

    Coupon updateCoupon(Long id);
}
