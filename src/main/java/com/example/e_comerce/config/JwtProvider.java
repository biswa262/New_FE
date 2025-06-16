package com.example.e_comerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection; // Keep this import if Authentication.getAuthorities() returns Collection
import java.util.Date;
import java.util.stream.Collectors; // Essential for stream operations

@Service
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // The default constructor is implicitly created by Lombok's @NoArgsConstructor
    // if you use it on the class or if no other constructors are defined.
    // Explicitly defining an empty constructor is fine, but often unnecessary.
    // public JwtProvider() {
    // }

    public String generateToken(Authentication auth) {
        // Correctly populate authorities directly here
        String authorities = auth.getAuthorities().stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 846000000)) // Consider shortening this for security
                .claim("email", auth.getName())
                .claim("authorities", authorities) // ADDING authorities to the JWT claims
                .signWith(key)
                .compact();

        return jwt;
    }

    public String getEmailFromToken(String jwt) {
        // Ensure that the "Bearer " prefix is removed before parsing
        // This method assumes the JWT passed here might still have it,
        // although JwtValidator should remove it before calling JwtProvider.
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }
}