package com.farmlink.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final CustomJwtVerificationFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("Configuring FarmLink Spring Security");

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
        // ✅ CORS (new way, no deprecation)
        .cors(Customizer.withDefaults())

        // ❌ CSRF disabled (JWT based)
        .csrf(csrf -> csrf.disable())

        // ✅ Stateless session
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // ✅ Authorization rules
        .authorizeHttpRequests(request -> request

            // 🔓 Public endpoints
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/api/auth/login",
                "/api/auth/register"
            ).permitAll()

            // 🔓 Preflight (CORS)
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // 🔐 FARMER APIs
            .requestMatchers("/farmers/**").hasRole("FARMER")
            .requestMatchers(HttpMethod.POST, "/rentals/farmer/**").hasRole("FARMER")
            .requestMatchers(HttpMethod.DELETE, "/rentals/farmer/**").hasRole("FARMER")
            .requestMatchers("/payments/**").hasRole("FARMER")

            // 🔐 OWNER APIs
            .requestMatchers("/owners/**").hasRole("OWNER")
            .requestMatchers(HttpMethod.PUT, "/rentals/owner/**").hasRole("OWNER")

            // 🔐 ADMIN APIs
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/reviews/**").hasRole("ADMIN")

            // 🔐 Any other request
            .anyRequest().authenticated()
        )

        // ✅ JWT Filter
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();

    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000"
        ));
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}