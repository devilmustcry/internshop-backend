package com.sandstorm.internshop.service;

import com.sandstorm.internshop.service.Order.OrderService;
import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.OrderProduct;
import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.exception.CustomerNotFound;
import com.sandstorm.internshop.exception.OrderNotFound;
import com.sandstorm.internshop.repository.OrderRepository;
import com.sandstorm.internshop.service.Customer.CustomerService;
import com.sandstorm.internshop.service.Order.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private OrderService orderService;

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
        request.setProductListRequestList(productList);

        Order order = new Order();
        order.setCustomer(testCustomer);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(customerService.getCustomer(any(Long.class))).thenReturn(testCustomer);

        Order responseOrder = orderService.createOrder(1L, request);

        assertThat(responseOrder.getCustomer()).isEqualToComparingFieldByField(testCustomer);
        verify(orderRepository, times(1)).save(any(Order.class));

    }

    @Test(expected = CustomerNotFound.class)
    public void createOrderFailToFindCustomer() {
        CreateOrderRequest.ProductListRequest product1 = new CreateOrderRequest.ProductListRequest();
        product1.setAmount(1);
        product1.setProductId(1L);

        CreateOrderRequest.ProductListRequest product2 = new CreateOrderRequest.ProductListRequest();
        product2.setAmount(1);
        product2.setProductId(2L);

        List<CreateOrderRequest.ProductListRequest> productList = Arrays.asList(product1, product2);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setProductListRequestList(productList);

        Order order = new Order();
        order.setCustomer(testCustomer);
        when(customerService.getCustomer(any(Long.class))).thenThrow(new CustomerNotFound("TEST"));

        Order responseOrder = orderService.createOrder(1L, request);
    }

    @Test
    public void getOrderByCustomerId() {
        Product product1 = new Product();
        product1.setPrice(10.0)
                .setName("Test");
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1)
                .setAmount(1)
                .setNetPrice(10.0);

        Product product2 = new Product();
        product1.setPrice(10.0)
                .setName("Test");
        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct1.setProduct(product1)
                .setAmount(1)
                .setNetPrice(10.0);
        List<OrderProduct> orderProductList = Arrays.asList(orderProduct1, orderProduct2);
        Order order1 = new Order();
        order1.setCustomer(testCustomer)
                .setPrice(100.0)
                .setDiscount(0.0)
                .setNetPrice(100.0)
                .setOrderProductList(orderProductList)
            ;
        Order order2 = new Order();
        order2.setPrice(100.0)
                .setDiscount(0.0)
                .setNetPrice(100.0)
                .setCustomer(testCustomer)
                .setOrderProductList(orderProductList)
            ;
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAllByCustomerId(any(Long.class))).thenReturn(orders);

        List<Order> responseOrder = orderService.getOrderByCustomerId(1L);

        assertThat(responseOrder.get(0).getCustomer()).isEqualToComparingFieldByField(testCustomer);
        assertThat(responseOrder.get(0).getNetPrice()).isEqualTo(100.0);
        assertThat(responseOrder.get(0).getDiscount()).isEqualTo(0);
        assertThat(responseOrder.get(0).getPrice()).isEqualTo(100.0);
        assertThat(responseOrder.get(0).getOrderProductList().get(0)).isEqualToComparingFieldByField(orderProduct1);
        assertThat(responseOrder.get(0).getOrderProductList().get(1)).isEqualToComparingFieldByField(orderProduct2);
        assertThat(responseOrder.get(1).getCustomer()).isEqualToComparingFieldByField(testCustomer);
        assertThat(responseOrder.get(1).getCustomer()).isEqualToComparingFieldByField(testCustomer);
        assertThat(responseOrder.get(1).getNetPrice()).isEqualTo(100.0);
        assertThat(responseOrder.get(1).getDiscount()).isEqualTo(0);
        assertThat(responseOrder.get(1).getPrice()).isEqualTo(100.0);
        assertThat(responseOrder.get(1).getOrderProductList().get(0)).isEqualToComparingFieldByField(orderProduct1);
        assertThat(responseOrder.get(1).getOrderProductList().get(1)).isEqualToComparingFieldByField(orderProduct2);
        verify(orderRepository, times(1)).findAllByCustomerId(any(Long.class));
    }

    @Test
    public void updateProductPrice() {
        Order beforeUpdate = new Order();
        beforeUpdate.setCustomer(testCustomer);
        beforeUpdate.setId(1L);
        beforeUpdate.setNetPrice(0.0);
        beforeUpdate.setPrice(0.0);
        beforeUpdate.setDiscount(0.0);
        Order afterUpdate = new Order();
        afterUpdate.setId(1L);
        afterUpdate.setCustomer(testCustomer);
        afterUpdate.setNetPrice(500.0);
        afterUpdate.setDiscount(100.0);
        afterUpdate.setPrice(600.0);
        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(beforeUpdate));
        when(orderRepository.save(any(Order.class))).thenReturn(afterUpdate);

        Order updatedOrder = orderService.updateOrderPrice(beforeUpdate.getId(), beforeUpdate);

        assertThat(updatedOrder).isEqualToComparingFieldByField(afterUpdate);
        verify(orderRepository, times(1)).findById(any(Long.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test(expected = OrderNotFound.class)
    public void updateProductPriceFailedToFindOrder() {
        Order beforeUpdate = new Order();
        beforeUpdate.setCustomer(testCustomer);
        beforeUpdate.setId(1L);
        beforeUpdate.setNetPrice(0.0);
        beforeUpdate.setPrice(0.0);
        beforeUpdate.setDiscount(0.0);
        Order afterUpdate = new Order();
        afterUpdate.setId(1L);
        afterUpdate.setCustomer(testCustomer);
        afterUpdate.setNetPrice(500.0);
        afterUpdate.setDiscount(100.0);
        afterUpdate.setPrice(600.0);
        when(orderRepository.findById(any(Long.class))).thenThrow(new OrderNotFound("TEST"));

        Order updateOrder = orderService.updateOrderPrice(beforeUpdate.getId(), beforeUpdate);
    }

    @Test
    public void countOrderByCustomerId() {
        Product product1 = new Product();
        product1.setPrice(10.0)
                .setName("Test");
        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setProduct(product1)
                .setAmount(1)
                .setNetPrice(10.0);

        Product product2 = new Product();
        product1.setPrice(10.0)
                .setName("Test");
        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct1.setProduct(product1)
                .setAmount(1)
                .setNetPrice(10.0);
        List<OrderProduct> orderProductList = Arrays.asList(orderProduct1, orderProduct2);
        Order order1 = new Order();
        order1.setCustomer(testCustomer)
                .setPrice(100.0)
                .setDiscount(0.0)
                .setNetPrice(100.0)
                .setOrderProductList(orderProductList)
        ;
        Order order2 = new Order();
        order2.setPrice(100.0)
                .setDiscount(0.0)
                .setNetPrice(100.0)
                .setCustomer(testCustomer)
                .setOrderProductList(orderProductList)
        ;
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.countByCustomer(any(Customer.class))).thenReturn(Long.valueOf(orders.size()));

        Long orderCount = orderService.countOrderByCustomerId(1L);
        assertThat(orderCount).isEqualTo(Long.valueOf(orders.size()));
    }
}