package com.example.e_comerce.controller;

import com.example.e_comerce.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse>homeController(){
        ApiResponse res= new ApiResponse("Welcome To Madan Web Services",true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
