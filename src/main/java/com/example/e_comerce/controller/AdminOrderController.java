// src/main/java/com/example/e_comerce/controller/AdminOrderController.java
package com.example.e_comerce.controller;

import java.util.List;

import com.example.e_comerce.exception.OrderException;
import com.example.e_comerce.model.Order;
import com.example.e_comerce.response.ApiResponse;
import com.example.e_comerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService=orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrdersHandler(
        // --- ADDED: JWT header for consistency as /api/admin/** now requires authentication ---
        @RequestHeader("Authorization") String jwt) {
        List<Order> orders=orderService.getAllOrders();
        // Even without role-based checks here, Spring Security ensures that
        // a valid JWT is present because of the .authenticated() rule in AppConfig.
        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> ConfirmedOrderHandler(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException, OrderException {
        Order order=orderService.confirmedOrder(orderId);
        return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shippedOrderHandler(@PathVariable Long orderId,
                                                     @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order=orderService.shippedOrder(orderId);
        return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliveredOrderHandler(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order=orderService.deliveredOrder(orderId);
        return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
    }

//    @PutMapping("/{orderId}/cancel")
//    public ResponseEntity<Order> canceledOrderHandler(@PathVariable Long orderId,
//                                                     @RequestHeader("Authorization") String jwt) throws OrderException{
//        Order order=orderService.cancledOrder(orderId);
//        return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
//    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId,
                                                          @RequestHeader("Authorization") String jwt) throws OrderException{
        orderService.deleteOrder(orderId);
        ApiResponse res=new ApiResponse("Order Deleted Successfully",true);
        System.out.println("delete method working....");
        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }
}