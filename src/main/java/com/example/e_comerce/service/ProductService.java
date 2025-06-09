package com.example.e_comerce.service;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Product;
import com.example.e_comerce.request.CreateProductRequest;
import com.example.e_comerce.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public ProductResponse createProduct(CreateProductRequest req);
    public String deleteProduct(Long ProductId) throws ProductException;
    public Product updateProduct(Long productId, Product req) throws ProductException;
    public Product findProductById(Long id) throws ProductException;
    public List<Product> getAllProducts();
    public List<Product> recentlyAddedProduct();
    public List<Product> searchProduct(String query);
    public List<Product> findProductByCategory(String category);
    public Page<Product> getAllProduct(String category,List<String>colors,List<String>sizes,Integer minPrice,Integer maxPrice, Integer minDiscount, String sort,String stock,Integer pageNumber,Integer pageSize);

}
