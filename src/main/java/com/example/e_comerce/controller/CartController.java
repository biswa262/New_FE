package com.example.e_comerce.controller;

import com.example.e_comerce.exception.CartItemException;
import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.Cart;
import com.example.e_comerce.model.CartItem;
import com.example.e_comerce.model.User;
import com.example.e_comerce.request.AddItemRequest;
import com.example.e_comerce.response.ApiResponse;
import com.example.e_comerce.service.CartService;
import com.example.e_comerce.service.CartItemService;
import com.example.e_comerce.service.UserService;
import jakarta.validation.Valid; // Ensure this import is present
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart") // Keeping the base path for cart operations
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService; // Inject CartItemService

    @GetMapping("/")
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        System.out.println("cart - " + cart.getUser().getEmail());
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@Valid @RequestBody AddItemRequest req, // Added @Valid here
                                                     @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(user.getId(), req); // Call the service method
        ApiResponse res = new ApiResponse("Item Added To Cart Successfully", true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    // --- NEW: Endpoint to update a cart item ---
    @PutMapping("/items/{cartItemId}") // Endpoint for updating a specific cart item
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem, // Assuming CartItem has the updateable fields (e.g., quantity)
            @RequestHeader("Authorization") String jwt)
            throws CartItemException, UserException { // Declaring exceptions for propagation

        User user = userService.findUserProfileByJwt(jwt); // Get user from JWT
        // Call cartItemService to update the item. The service will handle quantity changes and price recalculations.
        CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
    }

    // --- NEW: Endpoint to remove a cart item ---
    @DeleteMapping("/items/{cartItemId}") // Endpoint for deleting a specific cart item
    public ResponseEntity<ApiResponse> removeCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt)
            throws CartItemException, UserException { // Declaring exceptions for propagation

        User user = userService.findUserProfileByJwt(jwt); // Get user from JWT
        // Call cartItemService to remove the item
        cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse res = new ApiResponse("Item removed from cart successfully", true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}