package com.sandstorm.internshop.security;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.exception.CustomerNotFound;
import com.sandstorm.internshop.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomerDetailServiceImpl implements CustomerDetailService {

    private final CustomerRepository customerRepository;

    public CustomerDetailServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(s).orElseThrow(() -> new CustomerNotFound("Cannot find customer with username: " + s));
        return UserPrincipal.create(customer);
    }

    @Override
    @Transactional
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFound("Cannot find customer with id: " + id));
        return UserPrincipal.create(customer);
    }
}
