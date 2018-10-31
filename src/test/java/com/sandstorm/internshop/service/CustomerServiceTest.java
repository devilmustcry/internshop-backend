package com.sandstorm.internshop.service;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.exception.CustomerNotFound;
import com.sandstorm.internshop.repository.CustomerRepository;
import com.sandstorm.internshop.service.Customer.CustomerService;
import com.sandstorm.internshop.service.Customer.CustomerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() {
        customerService = new CustomerServiceImpl(customerRepository, bCryptPasswordEncoder);
    }

    @Test
    public void createCustomerSuccessfully() {
        // Arrange
        Customer newCustomer = new Customer();
        newCustomer.setName("paiiza");
        newCustomer.setUsername("test");
        newCustomer.setPassword("1234");

        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("1234");

        // Act
        Customer customerResponse = customerService.createCustomer(newCustomer);

        // Assert
        assertThat(customerResponse.getName()).isEqualTo("paiiza");
        assertThat(customerResponse.getUsername()).isEqualTo("test");
        assertThat(customerResponse.getPassword()).isEqualTo("1234");

        verify(customerRepository, times(1)).save(newCustomer);
        verify(bCryptPasswordEncoder, times(1)).encode(any(String.class));

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
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(getCustomer));

        Customer responseCustomer = customerService.getCustomer(1L);

        assertThat(responseCustomer.getName()).isEqualTo("GET");
        assertThat(responseCustomer.getUsername()).isEqualTo("GETUSER");
        assertThat(responseCustomer.getPassword()).isEqualTo("GETPASS");
        verify(customerRepository, times(1)).findById(any(Long.class));
    }

    @Test(expected = CustomerNotFound.class)
    public void getCustomerFailed() throws Throwable {
        when(customerRepository.findById(any(Long.class))).thenThrow(new CustomerNotFound("TEST"));

        Customer customer = customerService.getCustomer(1L);

//        verify(customerRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void getCustomerByUsernameSuccessfully() throws Throwable {
        Customer getCustomer = new Customer();
        getCustomer.setName("GET");
        getCustomer.setUsername("GETUSER");
        getCustomer.setPassword("GETPASS");
        when(customerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(getCustomer));

        Customer responseCustomer = customerService.getCustomerByUsername("GETUSER");

        assertThat(responseCustomer.getName()).isEqualTo("GET");
        assertThat(responseCustomer.getUsername()).isEqualTo("GETUSER");
        assertThat(responseCustomer.getPassword()).isEqualTo("GETPASS");
        verify(customerRepository, times(1)).findByUsername(any(String.class));
    }

    @Test(expected = CustomerNotFound.class)
    public void getCustomerByUsernameFailed() throws Throwable {
        when(customerRepository.findByUsername(any(String.class))).thenThrow(new CustomerNotFound("TEST"));

        Customer customer = customerService.getCustomerByUsername("TEST");

//        verify(customerRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void loadUserWhenLogin() {
        Customer testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("TEST");
        testCustomer.setUsername("id");
        testCustomer.setPassword("password");

        CustomerService mockCustomerService = Mockito.spy(customerService);
        doReturn(testCustomer).when(mockCustomerService).getCustomerByUsername(any(String.class));

        UserDetails customer = mockCustomerService.loadUserByUsername("id");
        assertThat(customer.getUsername()).isEqualTo("id");
        assertThat(customer.getPassword()).isEqualTo("password");
        verify(mockCustomerService, times(1)).getCustomerByUsername(any(String.class));
    }
}