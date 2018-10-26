package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.services.CustomerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
       return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAllCustomer();
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable(value = "id") Long id) { return customerService.getCustomer(id); }

    @GetMapping("/find")
    public Customer get(@RequestParam(value = "username") String username) {
        return customerService.getCustomerByUsername(username);
    }

}
