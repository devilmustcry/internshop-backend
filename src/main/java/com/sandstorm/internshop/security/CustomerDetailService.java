package com.sandstorm.internshop.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomerDetailService extends UserDetailsService {

    UserDetails loadUserById(Long id) throws UsernameNotFoundException;
}
