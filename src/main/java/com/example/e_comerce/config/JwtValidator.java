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
import org.springframework.security.core.authority.AuthorityUtils; // Keep this import
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        // System.out.println("JwtValidator: Raw JWT header received: " + jwt); // Keep for debugging if needed

        if (jwt != null && jwt.startsWith("Bearer ")) {
            if (jwt.length() > 7) {
                jwt = jwt.substring(7);
                try {
                    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                    String email = String.valueOf(claims.get("email"));

                    // Get authorities string from claims
                    // Added a check for String "null" as well, for extra robustness
                    String authoritiesClaim = String.valueOf(claims.get("authorities"));
                    List<GrantedAuthority> auths;

                    if (authoritiesClaim != null && !authoritiesClaim.isEmpty() && !authoritiesClaim.equals("null")) {
                        auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim);
                    } else {
                        auths = List.of(); // Empty list if no authorities or "null" string
                    }

                    Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // System.out.println("JwtValidator: Token validated for email: " + email + " with authorities: " + auths); // Keep for debugging if needed
                } catch (Exception e) {
                    System.err.println("JwtValidator: Error validating token or setting security context: " + e.getMessage());
                    throw new BadCredentialsException("Invalid Token from JwtValidator: " + e.getMessage());
                }
            } else {
                System.err.println("JwtValidator: Authorization header too short after 'Bearer ' prefix: " + jwt);
                throw new BadCredentialsException("Invalid Authorization header: Token is missing or malformed.");
            }
        } else if (jwt != null && !jwt.startsWith("Bearer ")) {
            System.err.println("JwtValidator: Authorization header present but does not start with 'Bearer ': " + jwt);
            throw new BadCredentialsException("Invalid Authorization header format. Must start with 'Bearer '.");
        }

        filterChain.doFilter(request, response);
    }
}