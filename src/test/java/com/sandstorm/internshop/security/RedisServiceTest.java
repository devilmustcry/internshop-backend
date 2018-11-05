package com.sandstorm.internshop.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RedisServiceTest {

    private RedisService redisService;

    @Mock
    private Jedis jedis;

    private final String jwt = "jfiwoamneicv0qveplfkelqmfvl1emivejagoijnewaofiemwf";

    @Before
    public void setup() {
        redisService = new RedisServiceImpl(jedis);
    }

    @Test
    public void whiteList() {
        when(jedis.del(any(String.class))).thenReturn(any(Long.class));

        redisService.whiteList(jwt);

        verify(jedis, times(1)).del(any(String.class));
    }

    @Test
    public void blackList() {
        when(jedis.set(any(String.class), any(String.class))).thenReturn(jwt);

        redisService.blackList(jwt);

        verify(jedis, times(1)).set(any(String.class), any(String.class));
    }

    @Test
    public void isBlackListSuccess() {
        when(jedis.get(any(String.class))).thenReturn("true");

        Boolean isFound = redisService.isBlackList(jwt);

        assertThat(isFound).isTrue();

        verify(jedis, times(1)).get(any(String.class));

    }

    @Test
    public void isBlackListNotSuccess() {
        when(jedis.get(any(String.class))).thenReturn(null);

        Boolean isFound = redisService.isBlackList(jwt);

        assertThat(isFound).isFalse();

        verify(jedis, times(1)).get(any(String.class));

    }
}