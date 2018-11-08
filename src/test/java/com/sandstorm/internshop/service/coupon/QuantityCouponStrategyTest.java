package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.entity.product.OrderProduct;
import com.sandstorm.internshop.service.coupon.strategy.CouponStrategy;
import com.sandstorm.internshop.service.coupon.strategy.DiscountStrategy;
import com.sandstorm.internshop.service.coupon.strategy.PriceCouponStrategy;
import com.sandstorm.internshop.service.coupon.strategy.QuantityCouponStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class QuantityCouponStrategyTest {

    private CouponStrategy couponStrategy;

    @Mock
    private DiscountStrategy discountStrategy;

    private Order order;

    private Coupon coupon;

    @Before
    public void setup() {
        order = new Order();
        coupon = new Coupon();
        couponStrategy = new QuantityCouponStrategy(discountStrategy);
    }

    @Test
    public void discountSuccessful() {
        order.setOrderProductList(Arrays.asList(new OrderProduct().setAmount(3), new OrderProduct().setAmount(1)));
        coupon.setQuantityThreshold(1);

        Order discountedOrder = new Order().setNetPrice(0.5).setDiscount(0.5);
        when(discountStrategy.applyDiscount(any(Order.class), any(Coupon.class))).thenReturn(discountedOrder);

        Order processedOrder = couponStrategy.discount(order, coupon);

        assertThat(processedOrder).isEqualToComparingFieldByField(discountedOrder);
        verify(discountStrategy, times(1)).applyDiscount(any(Order.class), any(Coupon.class));
    }

    @Test
    public void discountFailed() {
        order.setOrderProductList(Arrays.asList(new OrderProduct().setAmount(1), new OrderProduct().setAmount(1)));
        coupon.setQuantityThreshold(3);

        Order discountedOrder = new Order().setNetPrice(0.5).setDiscount(0.5);

        Order processedOrder = couponStrategy.discount(order, coupon);

        assertThat(processedOrder).isEqualToComparingFieldByField(order);
        verify(discountStrategy, times(0)).applyDiscount(any(Order.class), any(Coupon.class));
    }
}
