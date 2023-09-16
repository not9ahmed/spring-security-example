package com.notahmed.springsecurityexample.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
// lombok
//@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @GetMapping("")
    public HashMap<String, String> home(){
        HashMap<String, String> hello = new HashMap<>();

        hello.put("message", "Hello Worlds");

        return hello;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authenticationService.register(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authentication(request));
    }


}
