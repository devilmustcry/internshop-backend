package com.sandstorm.internshop.wrapper.Order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class CreateOrderRequest {

    @JsonProperty("productList")
    private List<ProductListRequest> productListRequestList;

    @JsonProperty("code")
    private String couponCode;

    @Data
    public static class ProductListRequest {
        private Long productId;
        private Integer amount;
    }

    public Boolean hasCoupon() {
        return StringUtils.hasText(couponCode);
    }
}
