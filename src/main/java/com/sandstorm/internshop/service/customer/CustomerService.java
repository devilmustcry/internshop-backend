package com.sandstorm.internshop.service.customer;

import com.sandstorm.internshop.entity.product.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    List<Customer> getAllCustomer();

    Customer getCustomer(Long id);

    Customer getCustomerByUsername(String username);
}
