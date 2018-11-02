package com.sandstorm.internshop.entity.coupon;

import com.sandstorm.internshop.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "coupons")
@Data
public class Coupon extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    private CouponType couponType;

    @Column
    @NotNull
    private Integer available;

    @Column
    @NotNull
    private Double threshold;

    @Column
    @NotNull
    private Double discount;

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", couponType=" + couponType +
                ", available=" + available +
                ", threshold=" + threshold +
                ", discount=" + discount +
                '}';
    }
}
