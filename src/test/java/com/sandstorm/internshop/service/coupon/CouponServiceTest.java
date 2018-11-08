package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.coupon.CouponType;
import com.sandstorm.internshop.entity.coupon.DiscountType;
import com.sandstorm.internshop.entity.coupon.factory.CouponStrategyFactory;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.exception.CouponNotAvailable;
import com.sandstorm.internshop.exception.CouponNotFound;
import com.sandstorm.internshop.repository.coupon.CouponRepository;
import com.sandstorm.internshop.service.coupon.strategy.CouponStrategy;
import com.sandstorm.internshop.service.coupon.strategy.DiscountStrategy;
import com.sandstorm.internshop.service.orderproduct.OrderProductService;
import org.aspectj.weaver.ast.Or;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
@PrepareForTest(CouponStrategyFactory.class)
public class CouponServiceTest {

    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private OrderProductService orderProductService;

    @Mock
    private CouponStrategy couponStrategy;

    private CouponService mockCouponService;

    private Coupon coupon;

    @Before
    public void setup() {
        coupon = new Coupon()
                .setCode("TEST1")
                .setCouponType(CouponType.PRICE_THRESHOLD)
                .setDiscountType(DiscountType.FIXRATE)
                .setId(1L)
                .setAvailable(5)
                .setPriceThreshold(10.0)
                .setDiscount(5.0);

        couponService = new CouponServiceImpl(couponRepository, orderProductService);
        mockCouponService = spy(couponService);
    }

    @Test
    public void createCoupon() {

        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        Coupon newCoupon = couponService.createCoupon(coupon);

        assertThat(newCoupon).isEqualToComparingFieldByField(coupon);
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    public void getCouponByCodeSuccessfully() {

        when(couponRepository.findByCode(any(String.class))).thenReturn(Optional.of(coupon));

        Coupon foundCoupon = couponService.getCouponByCode("TEST1234");

        assertThat(foundCoupon).isEqualToComparingFieldByField(coupon);
        verify(couponRepository, times(1)).findByCode(any(String.class));

    }

    @Test(expected = CouponNotFound.class)
    public void getCouponByCodeFailed() {
        when(couponRepository.findByCode(any(String.class))).thenThrow(new CouponNotFound("TEST"));

        Coupon failedCoupon = couponService.getCouponByCode("TEST1234");
    }

    @Test
    public void useCouponSuccessfully() {
        Coupon afterUpdate = coupon;
        afterUpdate.setAvailable(4);

        when(couponRepository.findById(any(Long.class))).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(afterUpdate);

        Coupon updatedCoupon = couponService.useCoupon(1L);

        assertThat(updatedCoupon).isEqualToComparingFieldByField(afterUpdate);
        verify(couponRepository, times(1)).findById(any(Long.class));
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test(expected = CouponNotFound.class)
    public void failedToUseCouponCouponNotFound() {
        when(couponRepository.findById(any(Long.class))).thenThrow(new CouponNotFound("TEST"));

        Coupon updatedCoupon = couponService.useCoupon(1L);
    }

    @Test
    public void applyPriceCouponSuccessfully() {
        PowerMockito.mockStatic(CouponStrategyFactory.class);
        Order order = new Order()
                .setId(1L)
                .setNetPrice(1000.0)
                .setDiscount(0.0);
        Order discountOrder = new Order()
                .setId(1L)
                .setNetPrice(995.0)
                .setDiscount(5.0)
                .setIsUsedCoupon(true);

        doReturn(true).when(mockCouponService).validateCoupon(any(Coupon.class));
        doReturn(coupon).when(mockCouponService).useCoupon(any(Long.class));
        when(CouponStrategyFactory.create(any(CouponType.class), any(DiscountType.class))).thenReturn(couponStrategy);
        when(couponStrategy.discount(any(Order.class), any(Coupon.class))).thenReturn(discountOrder);

        Order newOrder = mockCouponService.applyCoupon(order, coupon);

        assertThat(newOrder).isEqualToComparingFieldByField(discountOrder);

        verify(mockCouponService, times(1)).validateCoupon(any(Coupon.class));
        verify(mockCouponService, times(1)).useCoupon(any(Long.class));
        verify(couponStrategy, times(1)).discount(any(Order.class), any(Coupon.class));
    }

    @Test
    public void failedToValidateCouponWhenApplyCoupon() {
        Order order = new Order()
                .setId(1L)
                .setNetPrice(1000.0)
                .setDiscount(0.0);
        doThrow(new CouponNotAvailable("TEST")).when(mockCouponService).validateCoupon(any(Coupon.class));

        Order newOrder = mockCouponService.applyCoupon(order, coupon);

        assertThat(newOrder).isEqualToComparingFieldByField(order);

        verify(mockCouponService, times(1)).validateCoupon(any(Coupon.class));
    }

    @Test
    public void validateCouponSuccessfully() {
        Coupon availableCoupon = new Coupon().setAvailable(1);

        Boolean isValidate = couponService.validateCoupon(availableCoupon);

        assertThat(isValidate).isTrue();
    }

    @Test(expected = CouponNotAvailable.class)
    public void failedToValidateCoupon() {
        Coupon notAvailableCoupon = new Coupon().setAvailable(0);
        Boolean isValidate = couponService.validateCoupon(notAvailableCoupon);
    }

}