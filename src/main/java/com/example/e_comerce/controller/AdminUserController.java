package com.example.e_comerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.User;
import com.example.e_comerce.service.UserService;
 
@RestController
@RequestMapping("api/admin")
public class AdminUserController {
	
	  @Autowired
	    private UserService userService;
	  
	 // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserException {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
 
    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
 
    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) throws UserException {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted successfully with ID " + id, HttpStatus.OK);
    }
 
}
