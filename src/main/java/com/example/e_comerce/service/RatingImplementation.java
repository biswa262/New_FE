package com.example.e_comerce.service;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Product;
import com.example.e_comerce.model.Rating;
import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.RatingRepository;
import com.example.e_comerce.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class RatingImplementation implements RatingService{

    private RatingRepository ratingRepository;
    private ProductService productService;

    public RatingImplementation(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository =ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating createRating(RatingRequest re, User user) throws ProductException {
        Product product=productService.findProductById(re.getProductId());
        Rating rating=new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(re.getRating());
        rating.setCreatedAt(LocalDateTime.now());

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        return ratingRepository.getAllProductsRating(productId);
    }
}
