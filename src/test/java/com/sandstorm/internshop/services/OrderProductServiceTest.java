package com.sandstorm.internshop.services;

import com.sandstorm.internshop.Wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.Customer;
import com.sandstorm.internshop.entity.Order;
import com.sandstorm.internshop.entity.OrderProduct;
import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.repository.OrderProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderProductServiceTest {

    private OrderProductServiceImpl orderProductService;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductService productService;

    private Customer testCustomer;
    private Product product1;
    private Product product2;

    @Before
    public void setUp() throws Exception {
        orderProductService = new OrderProductServiceImpl(orderProductRepository, productService);
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setUsername("Test");
        testCustomer.setPassword("TEST");
        testCustomer.setName("TestName");

        product1 = new Product();
        product1.setId(1L);
        product1.setName("TestProduct1");
        product1.setPrice(20.0);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("TestProduct2");
        product2.setPrice(50.0);
    }


    @Test
    public void createOrderProductSuccessfully() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomer(testCustomer);

        CreateOrderRequest.ProductListRequest productListed1 = new CreateOrderRequest.ProductListRequest();
        productListed1.setAmount(1);
        productListed1.setProductId(1L);

        CreateOrderRequest.ProductListRequest productListed2 = new CreateOrderRequest.ProductListRequest();
        productListed2.setAmount(3);
        productListed2.setProductId(2L);

        List<CreateOrderRequest.ProductListRequest> productListRequestList = Arrays.asList(productListed1, productListed2);

        Double netPrice = productListed1.getAmount() * product1.getPrice() + productListed2.getAmount() * product2.getPrice();
        when(productService.getProduct(1L)).thenReturn(product1);
        when(productService.getProduct(2L)).thenReturn(product2);

        Double responseNetPrice = orderProductService.createOrderProduct(order, productListRequestList);

        assertThat(responseNetPrice).isEqualTo(netPrice);
        verify(orderProductRepository, times(productListRequestList.size())).save(any(OrderProduct.class));

    }
}
