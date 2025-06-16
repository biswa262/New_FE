package com.example.e_comerce.controller;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Product;
import com.example.e_comerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; // Added import for Optional, though we'll use (required=false) directly
import org.springframework.lang.Nullable; // Added for @Nullable option, though (required=false) is sufficient

@RestController
@RequestMapping("/api")
public class UserProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(
        @RequestParam(required = false) String category, // Made optional
        @RequestParam(required = false) List<String> color, // Made optional
        @RequestParam(required = false) List<String> size, // Made optional
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice,
        @RequestParam(required = false) Integer minDiscount,
        @RequestParam(required = false) String sort, // Made optional
        @RequestParam(required = false) String stock, // Made optional
        @RequestParam(defaultValue = "0") Integer pageNumber, // Already had defaultValue, so also implied optional
        @RequestParam(defaultValue = "10") Integer pageSize // Already had defaultValue, so also implied optional
    ) {
        // IMPORTANT: When a List<String> @RequestParam is optional and not provided,
        // it might come as an empty list or null depending on Spring's version/config.
        // Your productService.getAllProduct method needs to handle null or empty lists gracefully.

        Page<Product> res = productService.getAllProduct(
            category,
            color,
            size,
            minPrice,
            maxPrice,
            minDiscount,
            sort,
            stock,
            pageNumber,
            pageSize
        );

        System.out.println("Complete products fetched.");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<Product>(product, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String query) {
        List<Product> products = productService.searchProduct(query);
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String name) {
        List<Product> result = productService.findProductByCategory(name);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}