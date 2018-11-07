package com.sandstorm.internshop.entity.coupon.factory;

import com.sandstorm.internshop.entity.coupon.CouponType;
import com.sandstorm.internshop.entity.coupon.DiscountType;
import com.sandstorm.internshop.service.coupon.strategy.*;

public class CouponStrategyFactory {

    public static CouponStrategy create(CouponType couponType, DiscountType discountType) {
        DiscountStrategy discountStrategy = DiscountStrategyFactory.create(discountType);
        switch (couponType) {
            case PRICE_AND_QUANTITY_THRESHOLD:
                return new PriceAndQuantityCouponStrategy(discountStrategy);
            case PRICE_THRESHOLD:
                return new PriceCouponStrategy(discountStrategy);
            case QUANTITY_THRESHOLD:
                return new QuantityCouponStrategy(discountStrategy);
            default:
                throw new RuntimeException("Type not found");
        }
    }
}
