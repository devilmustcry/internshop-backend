package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.coupon.CouponType;
import com.sandstorm.internshop.repository.coupon.CouponRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceTest {

    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

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
        couponService = new CouponServiceImpl(couponRepository);
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
}