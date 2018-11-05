package com.sandstorm.internshop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
@Setter
public class JWTTokenProvider {

    @Value("${auth.secret.key}")
    private String JWT_SECRET;

    @Value("${auth.token.prefix}")
    private String TOKEN_PREFIX;

//    @Value("${auth.token.prefix}")
//    String setUrl( final String TOKEN_PREFIX ) {
//         this.TOKEN_PREFIX  = TOKEN_PREFIX;
//    }

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String token = JWT.create()
                .withSubject(userPrincipal.getId().toString())
                .sign(Algorithm.HMAC512(JWT_SECRET.getBytes()));

        return token;
    }

    public Long getUserIdFromJWT(String token) {
        String jwt = JWT
                .require(Algorithm.HMAC512(JWT_SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        return Long.parseLong(jwt);
    }

    public String getJwtFromRequest(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }
        return null;
    }

    public boolean validate(String token) {
        String user = JWT
                .require(Algorithm.HMAC512(JWT_SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        if (user != null) {
            return true;
        }
        return false;
    }
}
