package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.coupon.factory.CouponStrategyFactory;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.exception.CouponNotAvailable;
import com.sandstorm.internshop.exception.CouponNotFound;
import com.sandstorm.internshop.repository.coupon.CouponRepository;
import com.sandstorm.internshop.service.orderproduct.OrderProductService;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private final OrderProductService orderProductService;

    public CouponServiceImpl(CouponRepository couponRepository, OrderProductService orderProductService) {
        this.couponRepository = couponRepository;
        this.orderProductService = orderProductService;
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
        try {
            if(this.validateCoupon(coupon)) {
                order = CouponStrategyFactory.create(coupon.getCouponType(), coupon.getDiscountType()).discount(order, coupon);
                this.useCoupon(coupon.getId());
            }
        } catch (RuntimeException e) {
            return order;
        }
        return order;
    }

    @Override
    public Boolean validateCoupon(Coupon coupon) {
        if (coupon.getAvailable() <= 0) {
            throw new CouponNotAvailable("Coupon not available anymore");
        }
        return true;
    }
}
