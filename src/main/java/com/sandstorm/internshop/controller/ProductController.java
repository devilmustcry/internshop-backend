package com.sandstorm.internshop.controller;

import com.sandstorm.internshop.wrapper.Base.BaseResponse;
import com.sandstorm.internshop.entity.Product;
import com.sandstorm.internshop.service.Product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public BaseResponse<List<Product>> getAllProduct()
    {

        return new BaseResponse<List<Product>>(HttpStatus.OK, "Get All Product Successfully", productService.getAllProduct());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Product>> createProduct(@RequestBody Product product) {
//        return new BaseResponse<Product>(HttpStatus.CREATED, "Create Pr Successfully", productService.createProduct(product));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BaseResponse<Product>(HttpStatus.CREATED,"Create Product Successfully", productService.createProduct(product)));
    }

    @GetMapping("/{id}")
    public BaseResponse<Product> getProduct(@PathVariable(name = "id") Long id) {
        return new BaseResponse<Product>(HttpStatus.OK,"Get A Product Successfully", productService.getProduct(id));
    }

}
