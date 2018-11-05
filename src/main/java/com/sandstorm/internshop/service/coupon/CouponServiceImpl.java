package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.coupon.CouponType;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.exception.CouponNotFound;
import com.sandstorm.internshop.repository.coupon.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code).orElseThrow(() -> new CouponNotFound("Cannot find coupon with code : " + code));
    }

    @Override
    public Coupon useCoupon(Long id) {
        return couponRepository.findById(id).map((coupon) -> {
            coupon.setAvailable(coupon.getAvailable() - 1);
            return couponRepository.save(coupon);
        }).orElseThrow(() -> new CouponNotFound("Cannot find coupon with id : " + id));
    }

    @Override
    public Order applyCoupon(Order order, Coupon coupon) {
        if (coupon.getCouponType().equals(CouponType.PRICE_THRESHOLD)) {
//            if (order.getNetPrice())
            
        } else if (coupon.getCouponType().equals(CouponType.QUANTITY_THRESHOLD)) {

        }
    }
}
