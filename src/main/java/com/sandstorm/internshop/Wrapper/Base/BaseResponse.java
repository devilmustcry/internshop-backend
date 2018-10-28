package com.sandstorm.internshop.Wrapper.Base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class BaseResponse<T> {

    @JsonProperty("status")
    private Integer responseStatus;

    private String message;

}
