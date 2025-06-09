package com.example.e_comerce.controller;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Product;
import com.example.e_comerce.request.CreateProductRequest;
import com.example.e_comerce.response.ApiResponse;
import com.example.e_comerce.response.ProductResponse;
import com.example.e_comerce.service.ProductService;
import jakarta.validation.Valid; // Import this
import org.springframework.beans.factory.annotation.Autowired; // Import this
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private ProductService productService;

    @Autowired // no need specifically
    //While Spring Boot 4.3+ implicitly autowires a single constructor, explicitly adding @Autowired to the constructor
    //is a good practice for clarity and consistency, especially when debugging. It makes the intent clear.
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProductHandler(@Valid @RequestBody CreateProductRequest req) throws ProductException {
        ProductResponse createdProduct = productService.createProduct(req);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException {
        System.out.println("dlete product controller .... ");
        String msg = productService.deleteProduct(productId);
        System.out.println("dlete product controller .... msg " + msg);
        ApiResponse res = new ApiResponse(msg, true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProduct() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Product>> recentlyAddedProduct() {
        List<Product> products = productService.recentlyAddedProduct();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProductHandler(@RequestBody Product req, @PathVariable Long productId) throws ProductException {
        Product updatedProduct = productService.updateProduct(productId, req);
        return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
    }
}
