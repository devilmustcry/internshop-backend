package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.security.CurrentUser;
import com.sandstorm.internshop.security.UserPrincipal;
import com.sandstorm.internshop.wrapper.Base.BaseResponse;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.service.Customer.CustomerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public BaseResponse<List<Customer>> getAll() {

        return new BaseResponse<List<Customer>>(HttpStatus.OK, "Get all customer successfully", customerService.getAllCustomer());
    }

    @GetMapping("/{id}")
    public BaseResponse<Customer> get(@PathVariable(value = "id") Long id) {
        return new BaseResponse<Customer>(HttpStatus.OK,"Get A Customer Successfully", customerService.getCustomer(id));
    }

    @GetMapping("/find")
    public BaseResponse<Customer> getByUsername(@RequestParam(value = "username") String username) {
        return new BaseResponse<Customer>(HttpStatus.OK, "Get Customer By Username Successfully", customerService.getCustomerByUsername(username));
    }


    @GetMapping("/test")
    public Long test(@CurrentUser Long user) {
        return user;
    }

}
