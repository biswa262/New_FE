package com.example.e_comerce.service;

import com.example.e_comerce.config.JwtProvider;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplemenation implements UserService{
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    public UserServiceImplemenation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User>user=userRepository.findById(userId);
        if (user.isPresent()){
            return user.get();
        }
        throw new UserException("User Not Find With ID"+userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException
    {
        System.out.println("user service");
        String email=jwtProvider.getEmailFromToken(jwt);

        System.out.println("email"+email);

        User user=userRepository.findByEmail(email);

        if(user==null) {
            throw new UserException("user not exist with email "+email);
        }
        System.out.println("email user"+user.getEmail());
        return user;
    }
}
