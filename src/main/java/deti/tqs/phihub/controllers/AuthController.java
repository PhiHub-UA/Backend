package deti.tqs.phihub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.dtos.LoginSchema;
import deti.tqs.phihub.dtos.RegisterSchema;
import deti.tqs.phihub.services.AuthService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private AuthService authService;

    private TokenProvider tokenService;

    private AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, TokenProvider tokenService,
            AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetails> createUser(@RequestBody @Valid RegisterSchema user) {
        UserDetails newUser = authService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginSchema user) {
        System.out.println("BANANANANANA");
        var authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        var authUser = authenticationManager.authenticate(authToken);
        var token = tokenService.generateAccessToken((User) authUser.getPrincipal());
        return ResponseEntity.ok(Map.of("token", token));
    }

}
