package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.service.coupon.strategy.DiscountStrategy;
import com.sandstorm.internshop.service.coupon.strategy.FixRateDiscountStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FixRateDiscountStrategyTest {

    private DiscountStrategy discountStrategy;

    private Order order;

    private Coupon coupon;

    @Before
    public void setup() {
        order = new Order();
        coupon = new Coupon();
        discountStrategy = new FixRateDiscountStrategy();
    }

    @Test
    public void discount() {
        order.setNetPrice(100.0)
            .setDiscount(0.0);
        coupon.setDiscount(50.0);

        Order discountedOrder = discountStrategy.applyDiscount(order, coupon);

        assertThat(discountedOrder.getDiscount()).isEqualTo(50.0);
        assertThat(discountedOrder.getNetPrice()).isEqualTo(50.0);
    }


}
