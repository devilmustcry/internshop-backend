package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.exception.CustomerNotFound;
import com.sandstorm.internshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    // TODO: add encryption
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFound("Cannot find customer with id: " + id));
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username).orElseThrow(() -> new CustomerNotFound("Cannot find customer with username: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = getCustomerByUsername(username);
        return new User(customer.getUsername(), customer.getPassword(), emptyList());
    }

//    @Bean
//    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
