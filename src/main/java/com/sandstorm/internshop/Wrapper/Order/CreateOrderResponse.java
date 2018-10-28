package com.sandstorm.internshop.Wrapper.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sandstorm.internshop.Wrapper.Base.BaseResponse;
import lombok.Data;

@Data
public class CreateOrderResponse extends BaseResponse<CreateOrderResponse> {
    
    private Double netPrice;

}
