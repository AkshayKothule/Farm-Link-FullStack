package com.farmlink.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.farmlink.security.UserPrincipal;

@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private final RestTemplate restTemplate;

    public ApiLoggingInterceptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String apiPath = request.getRequestURI();
        String userInfo = "ANONYMOUS";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof UserPrincipal) {

            UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
            userInfo = "UserId=" + principal.getUserId()
                     + ", Role=" + principal.getUserRole();
        }

        String logMessage =
                "API Called: " + apiPath + " | " + userInfo;

        // Call .NET Logger API
        restTemplate.postForObject(
                "http://localhost:5000/logger",
                logMessage,
                String.class
        );

        return true;
    }
}
