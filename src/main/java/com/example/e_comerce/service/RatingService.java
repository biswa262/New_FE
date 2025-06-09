package com.example.e_comerce.service;


import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Rating;
import com.example.e_comerce.model.User;
import com.example.e_comerce.request.RatingRequest;

import java.util.List;

public interface RatingService {
    public Rating createRating(RatingRequest re, User user) throws ProductException;
    public List<Rating>getProductsRating(Long productId);

}
