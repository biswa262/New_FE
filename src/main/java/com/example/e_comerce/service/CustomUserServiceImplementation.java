package com.example.e_comerce.service;

import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // This import is now needed
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImplementation implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserServiceImplementation(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null){
            throw new UsernameNotFoundException("User Not Found with email: " + username);
        }

        // Re-implementing role-based authority creation
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Assuming user.getRole() returns a string like "ROLE_USER" or "ROLE_ADMIN"
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities // Now passing the list with the user's role
        );
    }
}