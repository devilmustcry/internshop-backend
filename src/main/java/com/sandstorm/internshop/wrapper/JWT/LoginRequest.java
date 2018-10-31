package com.sandstorm.internshop.wrapper.JWT;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;
}
