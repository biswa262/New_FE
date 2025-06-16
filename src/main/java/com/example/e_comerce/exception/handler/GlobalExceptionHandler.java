package com.example.e_comerce.exception.handler;

import com.example.e_comerce.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.exception.OrderException;
import com.example.e_comerce.exception.CartItemException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(UserException ex) {
        Map<String, String> error = new HashMap<>();
        // Modified: Add "[Global Error]" prefix to the message
        error.put("email", "[Global Error] " + ex.getMessage()); // Keep 'email' as key if that's what you expect
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentialsExceptionHandler(BadCredentialsException bce, WebRequest req) {
        System.err.println("GlobalExceptionHandler: Handling BadCredentialsException - " + bce.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] Invalid email or password. Please check your credentials.", false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ApiResponse> productExceptionHandler(ProductException pe, WebRequest req) {
        System.out.println("GlobalExceptionHandler: Handling ProductException - " + pe.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] " + pe.getMessage(), false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ApiResponse> orderExceptionHandler(OrderException oe, WebRequest req) {
        System.out.println("GlobalExceptionHandler: Handling OrderException - " + oe.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] " + oe.getMessage(), false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<ApiResponse> cartItemExceptionHandler(CartItemException cie, WebRequest req) {
        System.out.println("GlobalExceptionHandler: Handling CartItemException - " + cie.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] " + cie.getMessage(), false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            // Modified: Add "[Global Error]" prefix to each validation message
            errors.put(error.getField(), "[Global Error] " + error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> noHandlerFoundExceptionHandler(NoHandlerFoundException nhfe, WebRequest req) {
        System.err.println("GlobalExceptionHandler: Handling NoHandlerFoundException - " + nhfe.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] Resource Not Found: " + nhfe.getRequestURL(), false);
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> otherExceptionHandler(Exception e, WebRequest req) {
        System.err.println("GlobalExceptionHandler: Handling generic Exception - " + e.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] An unexpected error occurred: " + e.getMessage(), false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}