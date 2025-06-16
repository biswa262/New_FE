// src/main/java/com/example/e_comerce/config/JwtProvider.java
package com.example.e_comerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors; // This import was removed, re-adding it for clarity

@Service
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public JwtProvider() {
    }

    public String generateToken(Authentication auth) {
        // --- Re-added: Population of authorities from Authentication object and adding to claims ---
        // This ensures that the JWT token includes the user's roles/authorities,
        // which are then used by JwtValidator to set the user's security context.
        String authorities = auth.getAuthorities().stream()
                                   .map(GrantedAuthority::getAuthority)
                                   .collect(Collectors.joining(","));
        // --- END Re-added ---

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 846000000)) // Very long expiration, consider shortening for production
                .claim("email", auth.getName())
                // --- Re-added: Adding authorities to claims ---
                .claim("authorities", authorities)
                // --- END Re-added ---
                .signWith(key)
                .compact();

        return jwt;
    }

    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7); // Remove "Bearer " prefix
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }

    // --- Re-added: populateAuthorities method (can be useful if roles are used elsewhere, though not directly in generateToken) ---
    public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }
    // --- END Re-added ---
}