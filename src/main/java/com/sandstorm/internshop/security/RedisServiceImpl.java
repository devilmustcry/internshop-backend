package com.sandstorm.internshop.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final Jedis jedis;

    public RedisServiceImpl(Jedis jedis) {
        this.jedis = jedis;
    }

    public Boolean isBlackList(String jwt) {
        String isFound = jedis.get(jwt);
        if (isFound != null) {
            return true;
        }
        return false;
    }

    public void blackList(String jwt) {
        jedis.set(jwt, "true");
    }

    public void whiteList(String jwt) {
        jedis.del(jwt);
    }

}
