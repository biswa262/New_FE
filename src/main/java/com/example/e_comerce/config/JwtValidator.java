package com.example.e_comerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        System.out.println("JwtValidator: Raw JWT header received: " + jwt); // Added for debugging

        if (jwt != null && jwt.startsWith("Bearer ")) { // Check for null and prefix
            // Ensure the JWT string is long enough to safely call substring(7)
            if (jwt.length() > 7) {
                jwt = jwt.substring(7);
                try {
                    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                    String email = String.valueOf(claims.get("email"));
                    String authorities = String.valueOf(claims.get("authorities")); // Ensure this claim exists

                    List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("JwtValidator: Token validated for email: " + email + " with authorities: " + authorities); // Debugging
                } catch (Exception e) {
                    System.err.println("JwtValidator: Error validating token: " + e.getMessage()); // Debugging
                    throw new BadCredentialsException("Invalid Token from JwtValidator: " + e.getMessage()); // Add more context
                }
            } else {
                // Handle case where header is "Bearer " but no token, or too short
                System.err.println("JwtValidator: Authorization header too short after 'Bearer ' prefix: " + jwt); // Debugging
                throw new BadCredentialsException("Invalid Authorization header: Token is missing or malformed.");
            }
        } else if (jwt != null && !jwt.startsWith("Bearer ")) {
            System.err.println("JwtValidator: Authorization header present but does not start with 'Bearer ': " + jwt); // Debugging
            throw new BadCredentialsException("Invalid Authorization header format. Must start with 'Bearer '.");
        }
        // If jwt is null, it means no Authorization header was sent, and we just proceed to the next filter
        // Spring Security's later filters will handle unauthenticated access if required.

        filterChain.doFilter(request, response);
    }
}
