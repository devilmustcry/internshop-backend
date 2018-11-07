package com.sandstorm.internshop.entity.coupon;

import com.sandstorm.internshop.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="coupons")
@Data
@Accessors(chain = true)
public class Coupon extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name ="coupon_type")
    private CouponType couponType;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column
    @NotNull
    private Integer available;

    @Column(name = "price_threshold")
    @NotNull
    private Double priceThreshold;

    @Column(name = "quantity_threshold")
    @NotNull
    private Integer quantityThreshold;

    @Column
    @NotNull
    private Double discount;

}
