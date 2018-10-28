package com.sandstorm.internshop.services;

//import com.sandstorm.internshop.Wrapper.ProductListRequest;
import com.sandstorm.internshop.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    Product getProduct(Long id);

    List<Product> getAllProduct();

    Product createProduct(Product product);

//    List<Product> findAllByProductIds(List<ProductListRequest> productList);
}
