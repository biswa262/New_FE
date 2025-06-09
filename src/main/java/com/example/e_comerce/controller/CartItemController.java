package com.example.e_comerce.controller;

import com.example.e_comerce.exception.CartItemException;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.CartItem;
import com.example.e_comerce.model.User;
import com.example.e_comerce.response.ApiResponse;
import com.example.e_comerce.service.CartItemService;
import com.example.e_comerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping({"/api/cart_items"})
@Tag(
        name = "Cart Item Management",
        description = "create cart item delete cart item"
)
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;

    @DeleteMapping({"/{cartItemId}"})
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws CartItemException, UserException {
        User user = this.userService.findUserProfileByJwt(jwt);
        this.cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse res = new ApiResponse("Item Remove From Cart", true);
        return new ResponseEntity(res, HttpStatus.ACCEPTED);
    }

    @PutMapping({"/{cartItemId}"})
    public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItem cartItem, @RequestHeader("Authorization") String jwt) throws CartItemException, UserException {
        User user = this.userService.findUserProfileByJwt(jwt);
        CartItem updatedCartItem = this.cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return new ResponseEntity(updatedCartItem, HttpStatus.ACCEPTED);
    }
    

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(@RequestHeader("Authorization") String jwt) throws UserException {
        // Retrieve user based on JWT token
        User user = userService.findUserProfileByJwt(jwt);

        // Get cart item count for the user
        int cartItemCount = cartItemService.getCartItemCount(user.getId());

        // Return the cart item count as response
        return ResponseEntity.ok(cartItemCount);
    }



}
