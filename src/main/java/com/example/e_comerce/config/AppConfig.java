package com.example.e_comerce.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Import HttpMethod
import org.springframework.security.config.Customizer; // Import Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Recommended for CORS

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // 1. Allow unauthenticated access for authentication endpoints
                .requestMatchers("/auth/**").permitAll()

                // 2. Allow unauthenticated access for public product Browse (GET requests)
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                // 3. Restrict admin paths to ADMIN role only
                // This covers AdminProductController, AdminOrderController, AdminUserController which are under /api/admin/**
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 4. All other /api paths require either ADMIN or USER roles
                // This would cover user-specific functionalities like carts, orders etc.
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "USER")

                // 5. Any other request not explicitly matched above requires authentication by default
                // This is a safer default than permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless REST APIs
            .cors(Customizer.withDefaults()) // Use Customizer.withDefaults() to pick up the CorsConfigurationSource bean
            .httpBasic(Customizer.withDefaults()) // Re-added with Customizer
            .formLogin(Customizer.withDefaults()); // Re-added with Customizer

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Recommended: Define CorsConfigurationSource as a separate @Bean for clarity and reusability
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:4200",
                "http://localhost:1010"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Specify common methods
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Specify common headers
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Expose Authorization header for frontend
        configuration.setMaxAge(3600L); // How long the pre-flight request can be cached

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this config to all paths
        return source;
    }
}