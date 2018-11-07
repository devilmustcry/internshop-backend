package com.sandstorm.internshop.service.coupon.strategy;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.entity.product.OrderProduct;
import com.sandstorm.internshop.service.orderproduct.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceAndQuantityCouponStrategy extends CouponStrategy {

    public PriceAndQuantityCouponStrategy(DiscountStrategy discountStrategy) {
        super(discountStrategy);
    }

    @Override
    public Order discount(Order order, Coupon coupon) {
        if (order.getNetPrice() >= coupon.getPriceThreshold() && order.getOrderProductList().stream().mapToInt(OrderProduct::getAmount).sum() >= coupon.getQuantityThreshold()) {
           order = super.getDiscountStrategy().applyDiscount(order, coupon);
           order.setIsUsedCoupon(true);
        }
        return order;
    }
}
