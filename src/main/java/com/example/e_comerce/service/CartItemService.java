package com.example.e_comerce.service;

import com.example.e_comerce.exception.CartItemException;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.Cart;
import com.example.e_comerce.model.CartItem;
import com.example.e_comerce.model.Product;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItem(Long userId,Long id,CartItem cartItem)throws CartItemException, UserException;
    public CartItem isCartItemExist(Cart cart, Product product,String size,Long userId);
    public void removeCartItem(Long userId ,Long cartItemId) throws CartItemException,UserException;
    public CartItem findCartItemById(Long cartItemId) throws CartItemException;

    int getCartItemCount(Long userId);
}
