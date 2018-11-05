package com.sandstorm.internshop.wrapper.Order;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateOrderResponse {

    private Double netPrice;

    private Double price;

    private Double discount;

    private Boolean isUsedCoupon;

    private String couponResult;
}
