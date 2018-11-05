package com.sandstorm.internshop.service.product;

import com.sandstorm.internshop.entity.product.Customer;
import com.sandstorm.internshop.repository.product.CustomerRepository;
import com.sandstorm.internshop.security.CustomerDetailService;
import com.sandstorm.internshop.security.CustomerDetailServiceImpl;
import com.sandstorm.internshop.security.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerDetailServiceTest {

    private CustomerDetailService customerDetailService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    private UserPrincipal userPrincipal;

    @Before
    public void setUp() {
        customerDetailService = new CustomerDetailServiceImpl(customerRepository);
        testCustomer = new Customer()
                .setId(1L)
                .setName("TEST")
                .setUsername("TESTUSER")
                .setPassword("TESTPASS");

         userPrincipal = UserPrincipal.create(testCustomer);
    }

    @Test
    public void loadUserByUsername() {
        when(customerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testCustomer));

        UserDetails customer = customerDetailService.loadUserByUsername("TESTUSER");

        assertThat(customer).isEqualToComparingFieldByField(userPrincipal);
        verify(customerRepository, times(1)).findByUsername(any(String.class));

    }

    @Test
    public void loadUserById() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(testCustomer));

        UserDetails customer = customerDetailService.loadUserById(1L);

        assertThat(customer).isEqualToComparingFieldByField(userPrincipal);
        verify(customerRepository, times(1)).findById(any(Long.class));

    }
}