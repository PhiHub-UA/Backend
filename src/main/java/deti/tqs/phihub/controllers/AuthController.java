package deti.tqs.phihub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.DTOs.loginSchema;
import deti.tqs.phihub.DTOs.registerSchema;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.services.AuthService;

import jakarta.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    private TokenProvider tokenService;

    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, TokenProvider tokenService,
            AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody @Valid registerSchema user) {
        authService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody loginSchema user) {
        var authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        var authUser = authenticationManager.authenticate(authToken);
        var token = tokenService.generateAccessToken((User) authUser.getPrincipal());
        return ResponseEntity.ok(Map.of("token", token));
    }

}
