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

@RestController
@RequestMapping("/api")
public class UserProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(    @RequestParam String category,
                                                                          @RequestParam List<String> color,
                                                                          @RequestParam List<String> size,
                                                                          @RequestParam(required = false) Integer minPrice,
                                                                          @RequestParam(required = false) Integer maxPrice,
                                                                          @RequestParam(required = false) Integer minDiscount,
                                                                          @RequestParam String sort,
                                                                          @RequestParam String stock,
                                                                          @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize){


        Page<Product> res= productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort,stock,pageNumber,pageSize);

        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {

        Product product=productService.findProductById(productId);

        return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String query){

        List<Product> products=productService.searchProduct(query);

        return new ResponseEntity<List<Product>>(products,HttpStatus.OK);

    }


    @GetMapping("/category/{name}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String name) {
        List<Product> result = productService.findProductByCategory(name);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
