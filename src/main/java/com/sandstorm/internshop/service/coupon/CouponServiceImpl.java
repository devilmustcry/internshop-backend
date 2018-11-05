package com.sandstorm.internshop.service.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.entity.coupon.CouponType;
import com.sandstorm.internshop.entity.product.Order;
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
        order.setIsUsedCoupon(false);
        Double discount = order.getDiscount();
        Double netPrice = order.getNetPrice();
        if (this.validateCoupon(coupon)) {
            if (coupon.getCouponType().equals(CouponType.PRICE_THRESHOLD)) {
                if (order.getNetPrice() >= coupon.getThreshold()) {
                    order.setDiscount(discount + coupon.getDiscount());
                    order.setNetPrice(netPrice - coupon.getDiscount());
                    order.setIsUsedCoupon(true);
                    this.useCoupon(coupon.getId());
                }
            } else if (coupon.getCouponType().equals(CouponType.QUANTITY_THRESHOLD)) {
                if (orderProductService.countProductInOrder(order) >= coupon.getThreshold()) {
                    order.setDiscount(discount + coupon.getDiscount());
                    order.setNetPrice(netPrice - coupon.getDiscount());
                    order.setIsUsedCoupon(true);
                    this.useCoupon(coupon.getId());
                }
            }
        }
        return order;
    }

    @Override
    public Boolean validateCoupon(Coupon coupon) {
        return coupon.getAvailable() > 0;
    }
}
