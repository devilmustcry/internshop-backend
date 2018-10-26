package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    public void createCustomerSuccessfully() {
        Customer newCustomer = new Customer();
        newCustomer.setName("paiiza");
        newCustomer.setUsername("test");
        newCustomer.setPassword("1234");
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        Customer customerResponse = customerService.createCustomer(newCustomer);

        assertThat(customerResponse.getName()).isEqualTo("paiiza");
        assertThat(customerResponse.getUsername()).isEqualTo("test");
        assertThat(customerResponse.getPassword()).isEqualTo("1234");

        verify(customerRepository, times(1)).save(newCustomer);
//        ResultActions result = mockMvc.perform(post("/api/v1/customers")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsBytes(newCustomer)));

    }

    @Test
    public void getAllCustomerSuccessfully() {
        Customer customer1 = new Customer();
        customer1.setName("paiiza");
        customer1.setUsername("test");
        customer1.setPassword("1234");
        Customer customer2 = new Customer();
        customer2.setName("trongza");
        customer2.setUsername("za");
        customer2.setPassword("5678");
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        List<Customer> customerListResponse = customerService.getAllCustomer();

        assertThat(customerListResponse.get(0).getName()).isEqualTo("paiiza");
        assertThat(customerListResponse.get(0).getUsername()).isEqualTo("test");
        assertThat(customerListResponse.get(0).getPassword()).isEqualTo("1234");
        assertThat(customerListResponse.get(1).getName()).isEqualTo("trongza");
        assertThat(customerListResponse.get(1).getUsername()).isEqualTo("za");
        assertThat(customerListResponse.get(1).getPassword()).isEqualTo("5678");
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void getCustomerSuccessfully() throws Throwable {
        // Arrange
        Customer getCustomer = new Customer();
        getCustomer.setName("GET");
        getCustomer.setUsername("GETUSER");
        getCustomer.setPassword("GETPASS");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(getCustomer));

        Customer responseCustomer = customerService.getCustomer(1L);

        assertThat(responseCustomer.getName()).isEqualTo("GET");
        assertThat(responseCustomer.getUsername()).isEqualTo("GETUSER");
        assertThat(responseCustomer.getPassword()).isEqualTo("GETPASS");
        verify(customerRepository, times(1)).findById(1L);
    }
}