package com.example.e_comerce.controller;

import com.example.e_comerce.exception.OrderException;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.Address;
import com.example.e_comerce.model.Order;
import com.example.e_comerce.model.User;
import com.example.e_comerce.service.OrderService;
import com.example.e_comerce.service.UserService;
import jakarta.validation.Valid; // Import this
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

//    public OrderController(OrderService orderService, UserService userService) {
//        this.orderService = orderService;
//        this.userService = userService;
//    }

    @PostMapping("/")
    public ResponseEntity<Order> createOrderHandler(@Valid @RequestBody Address shippingAddress, // Added @Valid here, corrected param name
                                                     @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user, shippingAddress); // Used corrected param name
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }


    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistoryHandler(@RequestHeader("Authorization")
                                                                 String jwt) throws OrderException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        System.out.println(orders);
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization")
    String jwt) throws OrderException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Order orders = orderService.findOrderById(orderId);
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }
}