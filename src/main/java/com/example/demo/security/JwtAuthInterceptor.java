package com.example.demo.security;

import com.example.demo.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {
    
    private final JwtService jwtService;

    public JwtAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws IOException {
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")) {
            unauthorized(response, "Missing or invalid Authorization header");
            return false; // stop — controller never runs
        }


        try {
            String token = header.substring(7);
            Claims claims = jwtService.parse(token);
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("email", claims.get("email", String.class));
            return true;
        } catch (Exception e) {
            unauthorized(response, "Invalid or expired token");
            return false;
        }
    }


    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }



}
