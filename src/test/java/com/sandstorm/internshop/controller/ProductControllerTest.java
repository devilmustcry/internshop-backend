package com.sandstorm.internshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandstorm.internshop.entity.product.Product;
import com.sandstorm.internshop.exception.ProductNotFound;
import com.sandstorm.internshop.service.product.ProductServiceImpl;
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
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(secure = false)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductServiceImpl productService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void getAllProductSuccessfully() throws Exception {
        Product product1 = new Product();
        product1.setName("paiiza");
        product1.setPrice(1.0);
        Product product2 = new Product();
        product2.setName("trongza");
        product2.setPrice(2.0);
        when(productService.getAllProduct()).thenReturn(Arrays.asList(product1, product2));

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/products"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name", is("paiiza")))
                .andExpect(jsonPath("$.data[0].price", is(1.0)))
                .andExpect(jsonPath("$.data[1].name", is("trongza")))
                .andExpect(jsonPath("$.data[1].price", is(2.0)));
        verify(productService, times(1)).getAllProduct();
    }

    @Test
    public void createProductSuccessfully() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("paiiza");
        newProduct.setPrice(1.0);
        when(productService.createProduct(any(Product.class))).thenReturn(newProduct);

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newProduct)));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is("paiiza")))
                .andExpect(jsonPath("$.data.price", is(1.0)));
        verify(productService, times(1)).createProduct(newProduct);
    }

    @Test
    public void getProductSuccessfully() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("paiiza");
        newProduct.setPrice(1.0);
        when(productService.getProduct(any(Long.class))).thenReturn(newProduct);

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/products/1"));


        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("paiiza")))
                .andExpect(jsonPath("$.data.price", is(1.0)));
        verify(productService, times(1)).getProduct(any(Long.class));
    }

    @Test
    public void getProductFailed() throws Exception {
        when(productService.getProduct(any(Long.class))).thenThrow(new ProductNotFound("Test"));

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/products/1"));

        // Assert
        result.andExpect(status().isNotFound());
        verify(productService, times(1)).getProduct(any(Long.class));
    }

}
