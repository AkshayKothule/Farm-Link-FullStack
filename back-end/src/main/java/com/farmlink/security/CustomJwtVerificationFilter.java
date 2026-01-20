package com.farmlink.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmlink.dtos.ApiResponse;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtVerificationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1️⃣ Check Authorization header
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                log.debug("Bearer token detected");

                // 2️⃣ Extract JWT
                String jwt = authHeader.substring(7);

                // 3️⃣ Validate token
                Claims claims = jwtUtils.validateToken(jwt);

                // 4️⃣ Extract claims
                String userId = claims.get("user_id", String.class);
                String role = claims.get("user_role", String.class); // ROLE_FARMER / ROLE_OWNER
                String email = claims.getSubject();

                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(role));

                // 5️⃣ Create principal
                UserPrincipal principal =
                        new UserPrincipal(userId, email, null, null, role);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                authorities
                        );

                // 6️⃣ Store authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // continue filter chain
            filterChain.doFilter(request, response);

        } catch (Exception ex) {

            log.error("JWT authentication failed", ex);

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            ApiResponse apiResponse =
                    new ApiResponse("FAILED", ex.getMessage());

            response.getWriter()
                    .write(objectMapper.writeValueAsString(apiResponse));
        }
    }
}
