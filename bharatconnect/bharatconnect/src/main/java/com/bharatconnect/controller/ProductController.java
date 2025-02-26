package com.bharatconnect.controller;

import com.bharatconnect.model.Product;
import com.bharatconnect.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping("/analyze/{name}")
    public String analyzeProduct(@PathVariable String name) {
        return productService.analyzeProduct(name);
    }
}
