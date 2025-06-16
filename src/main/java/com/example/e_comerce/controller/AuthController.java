package com.example.e_comerce.controller;

import com.example.e_comerce.config.JwtProvider;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.UserRepository;
import com.example.e_comerce.request.LoginRequest;
import com.example.e_comerce.response.AuthResponse;
import com.example.e_comerce.service.CartService;
import com.example.e_comerce.service.CustomUserServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority; // Import this
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Import this
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList; // Import this
import java.util.List; // Import this

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider; //
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserServiceImplementation customUserServiceImplementation; //
    @Autowired
    private CartService cartService; //

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirst_name();
        String lastName = user.getLast_name();
        String mobile = user.getMobile();
        
        // Get the role string from the request, convert to lowercase for case-insensitive check
        String clientProvidedRole = user.getRole() != null ? user.getRole().toLowerCase() : null;
        String internalRole; // This will store "ROLE_USER" or "ROLE_ADMIN"

        // Check if email already exists
        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new UserException("Email is already used with another account");
        }

        // Validate the provided role and convert to internal format
        if ("user".equals(clientProvidedRole)) {
            internalRole = "ROLE_USER";
        } else if ("admin".equals(clientProvidedRole)) {
            internalRole = "ROLE_ADMIN";
        } else {
            throw new UserException("Invalid role specified. Only 'user' or 'admin' are allowed.");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirst_name(firstName);
        createdUser.setLast_name(lastName);
        createdUser.setMobile(mobile);
        createdUser.setRole(internalRole); // Set the internally standardized role

        User savedUser = userRepository.save(createdUser);
        cartService.createCart(savedUser);

        // --- CHANGES START HERE ---
        // 1. Create a list of GrantedAuthority objects for the user's role
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(savedUser.getRole())); // Add the role to authorities

        // 2. Create the UsernamePasswordAuthenticationToken with authorities
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUser.getEmail(),
            user.getPassword(), // Use the raw password from the request for initial authentication
            authorities // Pass the authorities here
        );
        // --- CHANGES END HERE ---

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("SignUp Success");
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@Valid @RequestBody LoginRequest loginRequest) { //
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("SignIn Success");
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username); //
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid UserName.....");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password.....");
        } else
            // Ensure authorities are passed here from UserDetails, which CustomUserServiceImplementation already does
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}