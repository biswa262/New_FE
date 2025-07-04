package com.example.e_comerce.controller;

import com.example.e_comerce.exception.UserException;

import com.example.e_comerce.model.User;
import com.example.e_comerce.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
 private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException{
        System.out.println("/api/users/profile");
        User user=userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }
    

    @PutMapping("/update")
	public ResponseEntity<User> updateUserProfile(
	 @RequestHeader("Authorization") String jwt,
	 @RequestBody User updatedUser) throws UserException {
		User user = userService.updateUserProfile(jwt, updatedUser);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
