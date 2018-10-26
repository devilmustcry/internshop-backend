package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    List<Customer> getAllCustomer();

    Customer getCustomer(Long id);

    Customer getCustomerByUsername(String username);
}
