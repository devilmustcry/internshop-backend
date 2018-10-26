package com.sandstorm.internshop.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class Constants {

//    @Value("${auth.secret.key}")
    public static final String SECRET = "cnjiwaoenvosfemkfwa2moipfm30imios";
//    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
//    @Value("${auth.token.prefix}")
    public static String TOKEN_PREFIX = "Bearer ";

//    @Value("${auth.token.header}")
    public static String TOKEN_HEADER = "Authorization";

//    @Value("${auth.signin.url}")
    public static String SIGN_IN_URL = "/api/v1/login";
}
