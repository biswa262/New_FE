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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserServiceImplementation customUserServiceImplementation;
    @Autowired
    private CartService cartService;

//    public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, CustomUserServiceImplementation customUserServiceImplementation, CartService cartService) {
//        this.userRepository = userRepository;
//        this.jwtProvider = jwtProvider;
//        this.passwordEncoder = passwordEncoder;
//        this.customUserServiceImplementation = customUserServiceImplementation;
//        this.cartService = cartService;
//    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws UserException{
        String email=user.getEmail();
        String password= user.getPassword();
        String firstName= user.getFirst_name();
        String lastName= user.getLast_name();
        // --- REMOVED: Role handling from signup process ---
        // Previously:
        // String role = user.getRole();
        // if (role == null || role.isEmpty()) {
        //     role = "ROLE_USER";
        // } else if (!role.equals("ROLE_USER") && !role.equals("ROLE_ADMIN")) {
        //     throw new UserException("Invalid role specified. Only 'ROLE_USER' and 'ROLE_ADMIN' are allowed.");
        // }
        // --- END REMOVAL ---

        User isEmailExist= userRepository.findByEmail(email);

        if (isEmailExist!=null){
            throw new UserException("Email is already Used with another account");
        }
        User createdUser= new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirst_name(firstName);
        createdUser.setLast_name(lastName);
        // --- REMOVED: Setting role on created user ---
        // Previously: createdUser.setRole(role);
        // --- END REMOVAL ---

        User savedUser= userRepository.save(createdUser);
        cartService.createCart(savedUser);

        // Note: userDetails.getAuthorities() from CustomUserServiceImplementation will now return an empty list
        Authentication authentication=new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token= jwtProvider.generateToken(authentication);

        AuthResponse authResponse= new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("SignUp Success");
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse>loginUserHandler(@Valid @RequestBody LoginRequest loginRequest){
        String username=loginRequest.getEmail();
        String password=loginRequest.getPassword();

        Authentication authentication =authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token= jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("SignIn Success");
        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);

    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails= customUserServiceImplementation.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid UserName.....");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password.....");
        }else
        // userDetails.getAuthorities() will now return an empty list of authorities
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}