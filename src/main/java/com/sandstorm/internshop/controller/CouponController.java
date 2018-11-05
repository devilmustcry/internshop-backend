package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.entity.coupon.Coupon;
import com.sandstorm.internshop.service.coupon.CouponService;
import com.sandstorm.internshop.wrapper.Base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Coupon>> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse<Coupon>(HttpStatus.CREATED,"Coupon has been created" , couponService.createCoupon(coupon)));
    }

    @GetMapping
    public BaseResponse<Coupon> getCouponByCode(@RequestParam(name = "code") String code) {
        return new BaseResponse<>(HttpStatus.OK, "Get Coupon successful", couponService.getCouponByCode(code));
    }
}
