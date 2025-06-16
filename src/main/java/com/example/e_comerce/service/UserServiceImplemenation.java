package com.example.e_comerce.service;

import com.example.e_comerce.config.JwtProvider;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImplemenation implements UserService{
	

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;
    

    @Autowired
    private PasswordEncoder passwordEncoder;


//    public UserServiceImplemenation(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

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
    
    @Override
    public User updateUserProfile(String jwt, User updatedUser) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            throw new UserException("User not found with email: " + email);
        }

        existingUser.setFirst_name(updatedUser.getFirst_name());
        existingUser.setLast_name(updatedUser.getLast_name());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    
//    @Override
//    public User updateUserProfile(String jwt, User updatedUser) throws UserException {
//        String email = jwtProvider.getEmailFromToken(jwt);
//        User existingUser = userRepository.findByEmail(email);
//
//        if (existingUser == null) {
//            throw new UserException("User not found with email: " + email);
//        }
//
//        // Update fields using correct model field names
//        existingUser.setFirst_name(updatedUser.getFirst_name());
//        existingUser.setLast_name(updatedUser.getLast_name());
//        existingUser.setEmail(updatedUser.getEmail());
//
//        return userRepository.save(existingUser);
//    }

}
