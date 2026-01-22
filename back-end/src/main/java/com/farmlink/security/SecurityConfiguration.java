package com.farmlink.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(request -> request

                // 🔓 Public
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/auth/login",
                    "/auth/register"
                ).permitAll()

                // Pre-flight
                .requestMatchers(HttpMethod.OPTIONS).permitAll()

                // 🔐 OWNER APIs
                .requestMatchers("/owners/**").hasRole("OWNER")

                // 🔐 FARMER APIs
                .requestMatchers("/farmers/**").hasRole("FARMER")

                // 🔐 RENTAL APIs (mixed)
                .requestMatchers(HttpMethod.POST, "/rentals/farmer/**").hasRole("FARMER")
                .requestMatchers(HttpMethod.DELETE, "/rentals/farmer/**").hasRole("FARMER")

                .requestMatchers(HttpMethod.PUT, "/rentals/owner/**").hasRole("OWNER")

                // 🔐 ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // any other
                .anyRequest().authenticated()
            )
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
}
