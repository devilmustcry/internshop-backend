package com.sandstorm.internshop.service.product;

//import com.sandstorm.internshop.wrapper.ProductListRequest;
import com.sandstorm.internshop.entity.product.Product;

import java.util.List;

public interface ProductService {

    Product getProduct(Long id);

    List<Product> getAllProduct();

    Product createProduct(Product product);

}
