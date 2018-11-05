package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.coupon.CouponType;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.exception.CouponNotAvailable;
import com.sandstorm.internshop.exception.CouponNotFound;
import com.sandstorm.internshop.repository.coupon.CouponRepository;
import com.sandstorm.internshop.service.orderproduct.OrderProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceTest {

    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private OrderProductService orderProductService;

    private CouponService mockCouponService;

    private Coupon priceCoupon;

    private Coupon quantityCoupon;

    @Before
    public void setup() {
        priceCoupon = new Coupon()
                .setCode("TEST1234")
                .setCouponType(CouponType.PRICE_THRESHOLD)
                .setId(1L)
                .setAvailable(5)
                .setThreshold(10.0)
                .setDiscount(5.0);
        quantityCoupon = new Coupon()
                .setCode("TEST4321")
                .setCouponType(CouponType.QUANTITY_THRESHOLD)
                .setId(1L)
                .setAvailable(5)
                .setThreshold(10.0)
                .setDiscount(5.0);
        couponService = new CouponServiceImpl(couponRepository, orderProductService);
        mockCouponService = spy(couponService);
    }

    @Test
    public void createCoupon() {

        when(couponRepository.save(any(Coupon.class))).thenReturn(priceCoupon);

        Coupon newCoupon = couponService.createCoupon(priceCoupon);

        assertThat(newCoupon).isEqualToComparingFieldByField(priceCoupon);
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    public void getCouponByCodeSuccessfully() {

        when(couponRepository.findByCode(any(String.class))).thenReturn(Optional.of(priceCoupon));

        Coupon foundCoupon = couponService.getCouponByCode("TEST1234");

        assertThat(foundCoupon).isEqualToComparingFieldByField(priceCoupon);
        verify(couponRepository, times(1)).findByCode(any(String.class));

    }

    @Test(expected = CouponNotFound.class)
    public void getCouponByCodeFailed() {
        when(couponRepository.findByCode(any(String.class))).thenThrow(new CouponNotFound("TEST"));

        Coupon failedCoupon = couponService.getCouponByCode("TEST1234");
    }

    @Test
    public void useCouponSuccessfully() {
        Coupon afterUpdate = priceCoupon;
        afterUpdate.setAvailable(4);

        when(couponRepository.findById(any(Long.class))).thenReturn(Optional.of(priceCoupon));
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
        Order order = new Order()
                .setId(1L)
                .setNetPrice(1000.0)
                .setDiscount(0.0);
        doReturn(true).when(mockCouponService).validateCoupon(any(Coupon.class));
        doReturn(priceCoupon).when(mockCouponService).useCoupon(any(Long.class));

        Order newOrder = mockCouponService.applyCoupon(order, priceCoupon);

        assertThat(newOrder.getNetPrice()).isEqualTo(995.0);
        assertThat(newOrder.getDiscount()).isEqualTo(5.0);
        assertThat(newOrder.getIsUsedCoupon()).isTrue();

        verify(mockCouponService, times(1)).validateCoupon(any(Coupon.class));
        verify(mockCouponService, times(1)).useCoupon(any(Long.class));
    }

    @Test
    public void failedToValidateCouponWhenApplyCoupon() {
        Order order = new Order()
                .setId(1L)
                .setNetPrice(1000.0)
                .setDiscount(0.0);
        doThrow(new CouponNotAvailable("TEST")).when(mockCouponService).validateCoupon(any(Coupon.class));

        Order newOrder = mockCouponService.applyCoupon(order, priceCoupon);

        assertThat(newOrder).isEqualToComparingFieldByField(order);

        verify(mockCouponService, times(1)).validateCoupon(any(Coupon.class));
    }

    @Test
    public void netPriceNotEnoughToApplyPriceCoupon() {
        Order order = new Order()
                .setId(1L)
                .setNetPrice(9.0)
                .setDiscount(0.0);
        doReturn(true).when(mockCouponService).validateCoupon(any(Coupon.class));
//        doReturn(priceCoupon).when(mockCouponService).useCoupon(any(Long.class));

        Order newOrder = mockCouponService.applyCoupon(order, priceCoupon);

        assertThat(newOrder.getNetPrice()).isEqualTo(9.0);
        assertThat(newOrder.getDiscount()).isEqualTo(0.0);
        assertThat(newOrder.getIsUsedCoupon()).isFalse();

        verify(mockCouponService, times(1)).validateCoupon(any(Coupon.class));
        verify(mockCouponService, times(0)).useCoupon(any(Long.class));
    }

    @Test
    public void applyQuantityCouponSuccessfully() {
        Order order = new Order()
                .setId(1L)
                .setNetPrice(1000.0)
                .setDiscount(0.0);
        doReturn(true).when(mockCouponService).validateCoupon(any(Coupon.class));
        doReturn(quantityCoupon).when(mockCouponService).useCoupon(any(Long.class));
        when(orderProductService.countProductInOrder(any(Order.class))).thenReturn(15L);

        Order newOrder = mockCouponService.applyCoupon(order, quantityCoupon);

        assertThat(newOrder.getNetPrice()).isEqualTo(995.0);
        assertThat(newOrder.getDiscount()).isEqualTo(5.0);
        assertThat(newOrder.getIsUsedCoupon()).isTrue();

        verify(mockCouponService, times(1)).validateCoupon(any(Coupon.class));
        verify(mockCouponService, times(1)).useCoupon(any(Long.class));
    }

    @Test
    public void quantityNotEnoughWhenApplyQuantityCoupon() {
        Order order = new Order()
                .setId(1L)
                .setNetPrice(1000.0)
                .setDiscount(0.0);
        doReturn(true).when(mockCouponService).validateCoupon(any(Coupon.class));
//        doReturn(quantityCoupon).when(mockCouponService).useCoupon(any(Long.class));
        when(orderProductService.countProductInOrder(any(Order.class))).thenReturn(1L);

        Order newOrder = mockCouponService.applyCoupon(order, quantityCoupon);

        assertThat(newOrder.getNetPrice()).isEqualTo(1000.0);
        assertThat(newOrder.getDiscount()).isEqualTo(0.0);
        assertThat(newOrder.getIsUsedCoupon()).isFalse();

        verify(mockCouponService, times(1)).validateCoupon(any(Coupon.class));
        verify(mockCouponService, times(0)).useCoupon(any(Long.class));
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