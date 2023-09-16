package com.notahmed.springsecurityexample.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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


    private final UserDetailsService userDetailsService;



    // Intercept every request
    // add header to response can be done here
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        System.out.println(request.getHeader("Authorization"));


        // to pass jwt inside header
        // header that contains the bearer token
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // check if the Authorization exists and if it starts with bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("inside if");
            filterChain.doFilter(request, response);

            // to stop execution of the filters
            return;
        }

        // get token after bearer space keyword
        jwt = authHeader.substring(7);

        System.out.println("jwt " + jwt);

        //extract user email from jwt token;
        userEmail = jwtService.extractUsername(jwt);

        // check if userEmail is not null and user is already authenticated
        // email exists in token and not authenticated and connected yet
        //


        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("userEmail  " + userEmail);
            // get user from database and check if it exists

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // check if token is valid
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // if token is valid then update SecurityContextHolder
                // send request DispatcherServlet


                // when we create user there is not credentials


                // needed by Spring and Security Context Holder to update Context Holder
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                        );


                // adding extra details to the token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)

                );

                // updating the context holder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        // header will be empty of not entered the if

        // pass to next filter
        filterChain.doFilter(request, response);


    }
}
