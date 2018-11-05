package com.sandstorm.internshop.service.product;

import com.sandstorm.internshop.service.orderproduct.OrderProductService;
import com.sandstorm.internshop.wrapper.Order.CreateOrderRequest;
import com.sandstorm.internshop.entity.product.Customer;
import com.sandstorm.internshop.entity.product.Order;
import com.sandstorm.internshop.entity.product.OrderProduct;
import com.sandstorm.internshop.entity.product.Product;
import com.sandstorm.internshop.repository.product.OrderProductRepository;
import com.sandstorm.internshop.service.order.OrderService;
import com.sandstorm.internshop.service.orderproduct.OrderProductServiceImpl;
import com.sandstorm.internshop.service.product.ProductService;
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
public class OrderProductServiceTest {

    private OrderProductService orderProductService;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    private Customer testCustomer;
    private Product product1;
    private Product product2;

    @Before
    public void setUp() throws Exception {
        orderProductService = new OrderProductServiceImpl(orderProductRepository, productService, orderService);
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
        order.setPrice(netPrice);
        order.setDiscount(0.0);
        order.setNetPrice(netPrice);
        when(productService.getProduct(1L)).thenReturn(product1);
        when(productService.getProduct(2L)).thenReturn(product2);
        when(orderService.countOrderByCustomerId(any(Long.class))).thenReturn(1L);

        Order newOrder = orderProductService.createOrderProducts(order, productListRequestList);

        assertThat(newOrder).isEqualToComparingFieldByField(order);
        verify(orderService, times(1)).countOrderByCustomerId(any(Long.class));
        verify(productService, times(productListRequestList.size())).getProduct(any(Long.class));
        verify(orderProductRepository, times(productListRequestList.size())).save(any(OrderProduct.class));
    }

    @Test
    public void createOrderProductSuccessfullyWithDiscount() {
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
        order.setPrice(netPrice);
        order.setDiscount(0.0);
        order.setNetPrice(netPrice);
        when(productService.getProduct(1L)).thenReturn(product1);
        when(productService.getProduct(2L)).thenReturn(product2);
        when(orderService.countOrderByCustomerId(any(Long.class))).thenReturn(5L);

        Order newOrder = orderProductService.createOrderProducts(order, productListRequestList);

        assertThat(newOrder.getPrice()).isEqualTo(order.getPrice());
        assertThat(newOrder.getDiscount()).isEqualTo(order.getPrice() * 0.1);
        assertThat(newOrder.getNetPrice()).isEqualTo(order.getPrice() - (order.getPrice() * 0.1));
        verify(orderService, times(1)).countOrderByCustomerId(any(Long.class));
        verify(productService, times(productListRequestList.size())).getProduct(any(Long.class));
        verify(orderProductRepository, times(productListRequestList.size())).save(any(OrderProduct.class));
    }

    @Test
    public void countProductInOrder() {
        Long count = 4L;
        when(orderProductRepository.countByOrder(any(Order.class))).thenReturn(count);

        Long counted = orderProductService.countProductInOrder(new Order().setId(1L));

        assertThat(counted).isEqualTo(count);

        verify(orderProductRepository, times(1)).countByOrder(any(Order.class));
    }
}
