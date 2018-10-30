package com.sandstorm.internshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.exception.CustomerNotFound;
import com.sandstorm.internshop.service.Customer.CustomerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(secure = false)
//@ActiveProfiles(value = "test")
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerServiceImpl customerService;

    @Autowired
    ObjectMapper objectMapper;

//    @Before
//    public void setUp() {
//
//
//    }

    @Test
    public void createUserSuccessfully() throws Exception {
        // Arrange
        Customer newCustomer = new Customer();
        newCustomer.setName("paiiza");
        newCustomer.setUsername("test");
        newCustomer.setPassword("1234");
        when(customerService.createCustomer(any(Customer.class))).thenReturn(newCustomer);

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newCustomer)));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("paiiza")))
                .andExpect(jsonPath("$.username", is("test")))
                .andExpect(jsonPath("$.password", is("1234")));
        verify(customerService, times(1)).createCustomer(newCustomer);
    }

    @Test
    public void getAllUserSuccessFully() throws Exception {
        // Arrange
        Customer customer1 = new Customer();
        customer1.setName("paiiza");
        customer1.setUsername("test");
        customer1.setPassword("1234");
        Customer customer2 = new Customer();
        customer2.setName("trongza");
        customer2.setUsername("za");
        customer2.setPassword("5678");
        when(customerService.getAllCustomer()).thenReturn(Arrays.asList(customer1, customer2));

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/customers"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name", is("paiiza")))
                .andExpect(jsonPath("$.data[0].username", is("test")))
                .andExpect(jsonPath("$.data[0].password", is("1234")))
                .andExpect(jsonPath("$.data[1].name", is("trongza")))
                .andExpect(jsonPath("$.data[1].username", is("za")))
                .andExpect(jsonPath("$.data[1].password", is("5678")));
        verify(customerService, times(1)).getAllCustomer();
    }

    @Test
    public void getUserSuccessfully() throws Exception {
        // Arrange
        Customer getCustomer = new Customer();
        getCustomer.setName("GET");
        getCustomer.setUsername("GETUSER");
        getCustomer.setPassword("GETPASS");
        when(customerService.getCustomer(any(Long.class))).thenReturn(getCustomer);

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/customers/1"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("GET")))
                .andExpect(jsonPath("$.data.username", is("GETUSER")))
                .andExpect(jsonPath("$.data.password", is("GETPASS")));
        verify(customerService, times(1)).getCustomer(any(Long.class));
    }

    @Test
    public void getUserFailed() throws Exception {
        // Arrange
        when(customerService.getCustomer(any(Long.class))).thenThrow(new CustomerNotFound("Test"));

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/customers/1"));

        // Assert
        result.andExpect(status().isNotFound());
        verify(customerService, times(1)).getCustomer(any(Long.class));
    }

    @Test
    public void getUserByUsername() throws Exception {
        Customer getCustomer = new Customer();
        getCustomer.setName("GET");
        getCustomer.setUsername("GETUSER");
        getCustomer.setPassword("GETPASS");
        when(customerService.getCustomerByUsername("GETUSER")).thenReturn(getCustomer);


        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/customers/find?username=GETUSER"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("GET")))
                .andExpect(jsonPath("$.data.username", is("GETUSER")))
                .andExpect(jsonPath("$.data.password", is("GETPASS")));
        verify(customerService, times(1)).getCustomerByUsername("GETUSER");
    }

    @Test
    public void getUserByUsernameFailed() throws Exception {
        // Arrange
        when(customerService.getCustomerByUsername(any(String.class))).thenThrow(new CustomerNotFound("Test"));

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/customers/find?username=GETUSER"));

        // Assert
        result.andExpect(status().isNotFound());
        verify(customerService, times(1)).getCustomerByUsername(any(String.class));

    }

}