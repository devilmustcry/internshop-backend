package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.security.JWTTokenProvider;
import com.sandstorm.internshop.security.RedisService;
import com.sandstorm.internshop.security.RedisServiceImpl;
import com.sandstorm.internshop.wrapper.Base.BaseResponse;
import com.sandstorm.internshop.wrapper.JWT.JWTResponse;
import com.sandstorm.internshop.wrapper.JWT.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final JWTTokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final RedisService redisService;

    public AuthController(AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider, RedisServiceImpl redisService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;
    }

    @PostMapping("/sign-in")
    public BaseResponse<JWTResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        redisService.whiteList(jwt);
        JWTResponse response = new JWTResponse();
        response.setToken(jwt);
        return new BaseResponse<JWTResponse>(HttpStatus.OK, "Login successfully", response);
    }

    @PostMapping("/sign-out")
    public BaseResponse<Boolean> unAuthenticateUser(@RequestHeader(value = "Authorization") String token) {
        String jwt = tokenProvider.getJwtFromRequest(token);
        redisService.blackList(jwt);
        return new BaseResponse<>(HttpStatus.OK, "Logout successfully", true);
    }
}
