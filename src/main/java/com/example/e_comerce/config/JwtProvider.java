package com.example.e_comerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
//import org.springframework.context.annotation.Primary; // Keep if Primary is used elsewhere
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority; // This import might become unused if populateAuthorities is removed
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
//import java.util.Collection; // This import might become unused
import java.util.Date;
//import java.util.HashSet; // This import might become unused
//import java.util.Set; // This import might become unused
//import java.util.stream.Collectors; // This import might become unused

@Service
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public JwtProvider() {
    }

    public String generateToken(Authentication auth) {
        // --- REMOVED: Population of authorities from Authentication object and adding to claims ---
        // Previously:
        // String authorities = auth.getAuthorities().stream()
        //                            .map(GrantedAuthority::getAuthority)
        //                            .collect(Collectors.joining(","));
        // --- END REMOVAL ---

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 846000000)) // Very long expiration, consider shortening
                .claim("email", auth.getName())
                // --- REMOVED: Adding authorities to claims ---
                // Previously:
                // .claim("authorities", authorities)
                // --- END REMOVAL ---
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

    // --- REMOVED: populateAuthorities method entirely ---
    // Previously:
    // public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
    //     Set<String> auths = new HashSet<>();
    //     for (GrantedAuthority authority : collection) {
    //         auths.add(authority.getAuthority());
    //     }
    //     return String.join(",", auths);
    // }
    // --- END REMOVAL ---
}