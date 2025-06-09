package com.example.e_comerce.service;

import com.example.e_comerce.exception.CartItemException;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.Cart;
import com.example.e_comerce.model.CartItem;
import com.example.e_comerce.model.Product;
import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.CartItemRepository;
import com.example.e_comerce.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImplementation implements CartItemService{


    private CartItemRepository cartItemRepository;
    private UserService userService;
    private CartRepository cartRepository;

    public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());
        CartItem createdCartItem=(CartItem)this.cartItemRepository.save(cartItem);

        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item=findCartItemById(id);
        User user=userService.findUserById(item.getUserId());

        if(user.getId().equals(userId)){
            item.setQuantity((cartItem.getQuantity()));
            item.setPrice(item.getQuantity()*item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice()*item.getQuantity());
            return (CartItem)this.cartItemRepository.save(item);
        }else
            throw new CartItemException("You can't update  another users cart_item");
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem=cartItemRepository.isCartItemExist(cart,product,size,userId);
        return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        System.out.println("userId- " + userId + " cartItemId " + cartItemId);
        CartItem cartItem=this.findCartItemById(cartItemId);
        User user=this.userService.findUserById(cartItem.getUserId());
        User reqUser=this.userService.findUserById(userId);
        if (user.getId().equals(reqUser.getId())) {
            this.cartItemRepository.deleteById(cartItemId);
        }else {
            throw new UserException("You Cant Remove Another Users Item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt=this.cartItemRepository.findById(cartItemId);
        if (opt.isPresent()){
            return (CartItem) opt.get();
        }
        throw new CartItemException("Cart Item Not Found With ID:" +cartItemId);
    }

    @Override
    public int getCartItemCount(Long userId) {
        return cartItemRepository.countByUserId(userId);
    }
}
