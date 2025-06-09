package com.example.e_comerce.service;

import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.model.Cart;
import com.example.e_comerce.model.CartItem;
import com.example.e_comerce.model.User;
import com.example.e_comerce.request.AddItemRequest;

public interface CartService {
    public Cart createCart(User user);
    public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException;
    public Cart findUserCart(Long userId);
}
