package com.example.e_comerce.service;

import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
// Removed: import org.springframework.security.core.authority.SimpleGrantedAuthority; // No longer needed if no authorities are added
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Keep if you want an empty list of authorities
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

        // --- REMOVED: Role-based authority creation ---
        // Previously:
        // List<GrantedAuthority> authorities = new ArrayList<>();
        // authorities.add(new SimpleGrantedAuthority(user.getRole()));
        // --- END REMOVAL ---

        // Now, pass an empty list of authorities if no roles are being used.
        // Spring Security will still allow authentication but won't apply role-based authorization.
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities // Now an empty list, as roles are removed
        );
    }
}