package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomerService extends UserDetailsService {

    Customer createCustomer(Customer customer);

    List<Customer> getAllCustomer();

    Customer getCustomer(Long id);

    Customer getCustomerByUsername(String username);
}
