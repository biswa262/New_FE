package com.example.e_comerce.exception.handler; // Corrected package name

import com.example.e_comerce.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.stream.Collectors;//binds all the errors

import com.example.e_comerce.exception.UserException;
import com.example.e_comerce.exception.ProductException;
import com.example.e_comerce.exception.OrderException;  
import com.example.e_comerce.exception.CartItemException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse> userExceptionHandler(UserException ue, WebRequest req) {
        System.out.println("GlobalExceptionHandler: Handling UserException - " + ue.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] " + ue.getMessage(), false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentialsExceptionHandler(BadCredentialsException bce, WebRequest req) {
        System.err.println("GlobalExceptionHandler: Handling BadCredentialsException - " + bce.getMessage());
        ApiResponse res = new ApiResponse("[Global Error] Invalid email or password. Please check your credentials.", false);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.UNAUTHORIZED); // Fixed 'new new' typo
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
    public ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException manve, WebRequest req) {
        System.err.println("GlobalExceptionHandler: Handling MethodArgumentNotValidException - " + manve.getMessage());

        String errorMessage = manve.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        if (errorMessage.isEmpty() && manve.getBindingResult().hasGlobalErrors()) {
            errorMessage = manve.getBindingResult().getGlobalErrors().stream()
                    .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        }
        if (errorMessage.isEmpty()) {
            errorMessage = "Validation failed for request body.";
        }

        ApiResponse res = new ApiResponse("[Global Error] Validation failed: " + errorMessage, false);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
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
