package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    private Customer testCustomer;

    @Before
    public void setUp() throws Exception {
         orderService = new OrderServiceImpl(orderRepository, customerService);
         testCustomer = new Customer();
         testCustomer.setId(1L);
         testCustomer.setUsername("Test");
         testCustomer.setPassword("TEST");
         testCustomer.setName("TestName");
    }

    @Test
    public void createOrderSuccessfully() {
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
//        order.setNetPrice(9999);
        order.setCustomer(testCustomer);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(customerService.getCustomer(any(Long.class))).thenReturn(testCustomer);

        Order responseOrder = orderService.createOrder(request);

        assertThat(responseOrder.getCustomer()).isEqualToComparingFieldByField(testCustomer);
        verify(orderRepository, times(1)).save(any(Order.class));

    }

    @Test
    public void getOrderByCustomerId() {
        Order order1 = new Order();
//        order1.setNetPrice(9999);
        order1.setCustomer(testCustomer);
        Order order2 = new Order();
        order2.setCustomer(testCustomer);
//        order2.setNetPrice(1234);
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAllByCustomerId(any(Long.class))).thenReturn(orders);

        List<Order> responseOrder = orderService.getOrderByCustomerId(1L);

//        assertThat(responseOrder.get(0).getNetPrice()).isEqualTo(9999.0);
        assertThat(responseOrder.get(0).getCustomer()).isEqualToComparingFieldByField(testCustomer);
//        assertThat(responseOrder.get(1).getNetPrice()).isEqualTo(1234.0);
        assertThat(responseOrder.get(1).getCustomer()).isEqualToComparingFieldByField(testCustomer);
        verify(orderRepository, times(1)).findAllByCustomerId(any(Long.class));
    }
}