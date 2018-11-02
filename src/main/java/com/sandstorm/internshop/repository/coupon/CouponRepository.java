package com.sandstorm.internshop.repository.coupon;

import com.sandstorm.internshop.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
