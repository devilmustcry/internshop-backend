package com.sandstorm.internshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.service.Customer.CustomerServiceImpl;
import com.sandstorm.internshop.service.OrderProduct.OrderProductServiceImpl;
import com.sandstorm.internshop.service.Order.OrderServiceImpl;
import org.junit.Before;
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

    @MockBean
    OrderProductServiceImpl orderProductService;

    @Autowired
    ObjectMapper objectMapper;

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
        CreateOrderRequest.ProductListRequest product1 = new CreateOrderRequest.ProductListRequest();
        product1.setAmount(1);
        product1.setProductId(1L);

        CreateOrderRequest.ProductListRequest product2 = new CreateOrderRequest.ProductListRequest();
        product2.setAmount(1);
        product2.setProductId(2L);

        List<CreateOrderRequest.ProductListRequest> productList = Arrays.asList(product1, product2);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(1L);
        request.setProductListRequestList(productList);

        Order order = new Order();
        order.setId(1L);
        order.setCustomer(testCustomer);
        order.setNetPrice(9999.0);
        order.setPrice(9999.0);
        order.setDiscount(0.0);
        when(orderService.createOrder(any(Long.class), any(CreateOrderRequest.class))).thenReturn(order);
        when(orderService.updateOrderPrice(any(Long.class), any(Order.class))).thenReturn(order);
        when(orderProductService.createOrderProducts(any(Order.class), any(List.class))).thenReturn(order);

        ResultActions result = mockMvc.perform(post("/api/v1/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.price", is(9999.0)))
                .andExpect(jsonPath("$.data.netPrice", is(9999.0)))
                .andExpect(jsonPath("$.data.discount", is(0.0)))
                .andExpect(jsonPath("$.status", is(201)));
        verify(orderProductService, times(1)).createOrderProducts(any(Order.class), any(List.class));
        verify(orderService, times(1)).createOrder(any(Long.class), any(CreateOrderRequest.class));
    }

    @Test
    public void getOrderByCustomer() throws Exception {
        Order order1 = new Order();
        order1.setNetPrice(9999.0);
        order1.setCustomer(testCustomer);
        Order order2 = new Order();
        order2.setCustomer(testCustomer);
        order2.setNetPrice(1234.0);
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderService.getOrderByCustomerId(any(Long.class))).thenReturn(orders);

        ResultActions result = mockMvc.perform(get("/api/v1/orders?customerId=1"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].netPrice", is(9999.0)))
                .andExpect(jsonPath("$.data[1].netPrice", is(1234.0)));
        verify(orderService, times(1)).getOrderByCustomerId(any(Long.class));
    }
}
