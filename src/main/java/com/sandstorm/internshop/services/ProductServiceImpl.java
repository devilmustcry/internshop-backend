package com.sandstorm.internshop.services;

import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.exception.ProductNotFound;
import com.sandstorm.internshop.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFound("Cannot find product with id : " + id));
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

//    @Override
//    public List<Product> findAllByProductIds(List<ProductListRequest> productList) {
//        log.info(productList.toString());
//        List<Long> ids = productList.stream().map(product -> product.getProductId()).collect(Collectors.toList());
//        return productRepository.findAllByIdIn(ids);
//    }
}
