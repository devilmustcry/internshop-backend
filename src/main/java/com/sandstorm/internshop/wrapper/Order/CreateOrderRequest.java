package com.sandstorm.internshop.wrapper.Order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @JsonProperty("productList")
    private List<ProductListRequest> productListRequestList;

    @Data
    public static class ProductListRequest {
        private Long productId;
        private Integer amount;
    }
}
