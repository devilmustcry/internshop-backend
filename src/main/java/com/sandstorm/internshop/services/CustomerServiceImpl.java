package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.exception.CustomerNotFound;
import com.sandstorm.internshop.repository.CustomerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
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

}
