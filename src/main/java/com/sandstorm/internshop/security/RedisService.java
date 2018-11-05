package com.sandstorm.internshop.security;

public interface RedisService {

    void whiteList(String jwt);

    void blackList(String jwt);

    Boolean isBlackList(String jwt);
}
