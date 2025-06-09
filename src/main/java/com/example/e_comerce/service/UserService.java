package com.example.e_comerce.service;

import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.User;
import org.springframework.stereotype.Service;


public interface UserService {
    public User findUserById(Long userId) throws UserException;
    public User findUserProfileByJwt(String jwt) throws UserException;
}