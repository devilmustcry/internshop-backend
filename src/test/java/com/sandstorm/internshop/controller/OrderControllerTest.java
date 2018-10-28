package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.services.CustomerServiceImpl;
import com.sandstorm.internshop.services.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(secure = false)
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderServiceImpl orderService;

    @MockBean
    CustomerServiceImpl customerService;

    private Customer testCustomer;

    @Before
    public void setUp() {
        testCustomer = new Customer();
        testCustomer.setName("Test");
        testCustomer.setUsername("TestUser");
        testCustomer.setId(1L);
        testCustomer.setPassword("TEST");
    }

    @Test
    public void createOrderSuccessfully() throws Exception {
        Order order = new Order();
//        order.setNetPrice(9999);
        when(customerService.getCustomer(any(Long.class))).thenReturn(testCustomer);
//        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        ResultActions result = mockMvc.perform(post("/api/v1/orders/1"));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.netPrice", is(9999.0)));
        verify(customerService, times(1)).getCustomer(any(Long.class));
//        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    public void getOrderByCustomer() throws Exception {
        Order order1 = new Order();
//        order1.setNetPrice(9999);
        order1.setCustomer(testCustomer);
        Order order2 = new Order();
        order2.setCustomer(testCustomer);
//        order2.setNetPrice(1234);
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderService.getOrderByCustomerId(any(Long.class))).thenReturn(orders);

        ResultActions result = mockMvc.perform(get("/api/v1/orders?customerId=1"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].netPrice", is(9999.0)))
                .andExpect(jsonPath("$[1].netPrice", is(1234.0)));
        verify(orderService, times(1)).getOrderByCustomerId(any(Long.class));
    }
}
