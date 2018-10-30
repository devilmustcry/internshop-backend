package com.sandstorm.internshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

public class SecurityConstants {
//    @Autowired
//    private static Environment environment;

//    @Value("${auth.secret.key}")
    public static final String SECRET = "cnjiwaoenvosfemkfwa2moipfm30imios";
//    public static final String SECRET = environment.getProperty("auth.secret.key");
//    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
//    @Value("${auth.token.prefix}")
    public static final String TOKEN_PREFIX = "Bearer ";

//    @Value("${auth.token.header}")
    public static final String TOKEN_HEADER = "Authorization";

//    @Value("${auth.signin.url}")
    public static final String SIGN_UP_URL = "/api/v1/customers";
}
