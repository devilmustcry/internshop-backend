package com.sandstorm.internshop.wrapper.Base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BaseResponse<T> {

    @JsonProperty("status")
    private Integer responseStatus;

    private String message;

    private T data;

    public BaseResponse(HttpStatus responseStatus, String message, T data) {
        this.responseStatus = responseStatus.value();
        this.message = message;
        this.data = data;
    }
}
