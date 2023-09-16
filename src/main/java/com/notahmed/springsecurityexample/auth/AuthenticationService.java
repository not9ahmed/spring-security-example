package com.notahmed.springsecurityexample.auth;

import com.notahmed.springsecurityexample.config.JwtService;
import com.notahmed.springsecurityexample.user.Role;
import com.notahmed.springsecurityexample.user.User;
import com.notahmed.springsecurityexample.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

// lombok
//@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    // will handle the register process
    public AuthenticationResponse register(RegisterRequest request) {

        // TODO change it
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    // will handle the authentication process
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));


        var jwtToken = jwtService.generateToken(user);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }
}
