package com.sandstorm.internshop.service;

import com.sandstorm.internshop.entity.product.Product;
import com.sandstorm.internshop.repository.product.ProductRepository;
import com.sandstorm.internshop.service.product.ProductService;
import com.sandstorm.internshop.service.product.ProductServiceImpl;
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
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;


    @Before
    public void setUp() throws Exception {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void getProduct() {
        Product newProduct = new Product();
        newProduct.setName("paiiza");
        newProduct.setPrice(1.0);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(newProduct));

        Product responseProduct = productService.getProduct(1L);

        assertThat(responseProduct.getName()).isEqualTo("paiiza");
        assertThat(responseProduct.getPrice()).isEqualTo(1);
        verify(productRepository, times(1)).findById(any(Long.class));

    }

    @Test
    public void getAllProduct() {
        Product product1 = new Product();
        product1.setName("paiiza");
        product1.setPrice(1.0);
        Product product2 = new Product();
        product2.setName("trongza");
        product2.setPrice(2.0);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> productListResponse = productService.getAllProduct();

        assertThat(productListResponse.get(0).getName()).isEqualTo("paiiza");
        assertThat(productListResponse.get(0).getPrice()).isEqualTo(1);
        assertThat(productListResponse.get(1).getName()).isEqualTo("trongza");
        assertThat(productListResponse.get(1).getPrice()).isEqualTo(2);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void createProduct() {
        Product newProduct = new Product();
        newProduct.setName("paiiza");
        newProduct.setPrice(1.0);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        Product productResponse = productService.createProduct(newProduct);

        assertThat(productResponse.getName()).isEqualTo("paiiza");
        assertThat(productResponse.getPrice()).isEqualTo(1);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
