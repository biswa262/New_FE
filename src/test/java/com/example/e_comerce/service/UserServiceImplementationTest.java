package com.example.e_comerce.service;

import com.example.e_comerce.config.JwtProvider;
import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.model.User;
import com.example.e_comerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserServiceImplemenation userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for findUserById - success
    @Test
    void testFindUserById_Success() throws UserException {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    // Test for findUserById - user not found
    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            userService.findUserById(1L);
        });

        assertEquals("User Not Find With ID1", exception.getMessage());
    }

    // Test for findUserProfileByJwt - success
    @Test
    void testFindUserProfileByJwt_Success() throws UserException {
        String jwt = "valid.jwt.token";
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);

        when(jwtProvider.getEmailFromToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findUserProfileByJwt(jwt);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    // Test for findUserProfileByJwt - user not found
    @Test
    void testFindUserProfileByJwt_UserNotFound() {
        String jwt = "invalid.jwt.token";
        String email = "notfound@example.com";

        when(jwtProvider.getEmailFromToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(null);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.findUserProfileByJwt(jwt);
        });

        assertEquals("user not exist with email " + email, exception.getMessage());
    }
}
