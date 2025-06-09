package com.example.e_comerce.service;

import com.example.e_comerce.exception.OrderException;
import com.example.e_comerce.model.Address;
import com.example.e_comerce.model.Order;
import com.example.e_comerce.model.User;

import java.util.List;

public interface OrderService  {

    public Order createOrder(User user, Address shippingAddress);
    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> usersOrderHistory(Long userId);
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order cancelledOrder(Long orderId) throws OrderException;
    public  List<Order>getAllOrders();
    public void deleteOrder(Long orderId) throws OrderException;
    Order confirmedOrder(Long orderId) throws OrderException;
    //12345 9 10



    public  Order placedOrder(Long orderId) throws OrderException;
    public Order orderConfirmedOrder(Long orderId) throws OrderException;
}
