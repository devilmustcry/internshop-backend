package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.security.JWTTokenProvider;
import com.sandstorm.internshop.wrapper.Base.BaseResponse;
import com.sandstorm.internshop.wrapper.JWT.JWTResponse;
import com.sandstorm.internshop.wrapper.JWT.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JWTTokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
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
        JWTResponse response = new JWTResponse();
        response.setToken(jwt);
        return new BaseResponse<JWTResponse>(HttpStatus.OK, "Login successfully", response);
    }
}
