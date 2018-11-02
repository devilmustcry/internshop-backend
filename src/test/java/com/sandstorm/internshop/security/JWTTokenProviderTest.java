package com.sandstorm.internshop.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.sandstorm.internshop.entity.product.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JWTTokenProviderTest {

    private JWTTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    Customer customer;

    private final String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIn0.u0w8lG6RGAKlhIDBdf-Y5gQnUxzBGS-ojpjwFo48_bZurixlvJ__VWij76-7QRYa56iGl_0HNXFwGcLQ4G2ftA";

    private final String failedJWTToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIn0.u0w8lG6RGAKlhIDBdf-Y5gQnUxzBGS-ojpjwFo48_bZurixlvJ__VWij76-7QRYa56iGl_0HNXFwGcLQ4G2ft";

    @Before
    public void setUp() {
        jwtTokenProvider = new JWTTokenProvider();
        jwtTokenProvider.setJWT_SECRET("SECRET");
        jwtTokenProvider.setTOKEN_PREFIX("Bearer ");
        customer = new Customer()
                .setId(1L)
                .setUsername("TESTUSER")
                .setPassword("1234")
                .setName("TEST");

    }

    @Test
    public void generateToken() {
        when(authentication.getPrincipal()).thenReturn(UserPrincipal.create(customer));

        String token = jwtTokenProvider.generateToken(authentication);

        assertThat(token).isNotNull();
    }

    @Test
    public void getUserIdFromJWT() {
        Long userId = jwtTokenProvider.getUserIdFromJWT(jwtToken);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    public void validateSuccess() {

        Boolean isSuccess = jwtTokenProvider.validate(jwtToken);

        assertThat(isSuccess).isTrue();

    }

    @Test(expected = SignatureVerificationException.class)
    public void validateFailed() {

        Boolean isSuccess = jwtTokenProvider.validate(failedJWTToken);

    }
}