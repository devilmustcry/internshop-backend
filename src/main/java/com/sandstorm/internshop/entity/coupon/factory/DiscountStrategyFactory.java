package com.sandstorm.internshop.entity.coupon.factory;

import com.sandstorm.internshop.entity.coupon.DiscountType;
import com.sandstorm.internshop.service.coupon.strategy.DiscountStrategy;
import com.sandstorm.internshop.service.coupon.strategy.FixRateDiscountStrategy;
import com.sandstorm.internshop.service.coupon.strategy.PercentageDiscountStrategy;

public class DiscountStrategyFactory {

    public static DiscountStrategy create(DiscountType discountType) {
        switch (discountType) {
            case FIXRATE:
                return new FixRateDiscountStrategy();
            case PERCENTAGE:
                return new PercentageDiscountStrategy();
            default:
                throw new RuntimeException("Discount type not found");

        }
    }
}
