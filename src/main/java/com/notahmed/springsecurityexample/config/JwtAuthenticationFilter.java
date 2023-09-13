package com.notahmed.springsecurityexample.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
RequiredArgsConstructor will create constructor for the private final fields aka Dependency Injection
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;





    // Intercept every request
    // add header to response can be done here
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        // to pass jwt inside header
        // header that contains the bearer token
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // check if the Authorization exists and if it starts with bearer
        if (authHeader == null || !authHeader.startsWith("bearer ")) {
            filterChain.doFilter(request, response);

            // to stop execution of the filters
            return;
        }

        // get token after bearer space keyword
        jwt = authHeader.substring(7);

        //extract user email from jwt token;
        userEmail = jwtService.extractUsername(jwt);

        // check if userEmail is not null and user is already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
        }
    }
}
