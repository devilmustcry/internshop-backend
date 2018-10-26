package com.sandstorm.internshop.security;

import com.sandstorm.internshop.services.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.sandstorm.internshop.security.Constants.SIGN_IN_URL;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private CustomerService customerService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(CustomerService customerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerService = customerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_IN_URL).permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/customers").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerService).passwordEncoder(bCryptPasswordEncoder);
    }
}
