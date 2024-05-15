package deti.tqs.phihub.controllers.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.dtos.LoginSchema;
import deti.tqs.phihub.dtos.RegisterSchema;
import deti.tqs.phihub.services.AuthService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

        if (newUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginSchema user) {

        var authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());

        Authentication authUser = authenticationManager.authenticate(authToken);

        String token = null;
        if (user.role().equals("user")) {
            token = tokenService.generateAccessToken((User) authUser.getPrincipal());
        } else if (user.role().equals("medic")) {
            token = tokenService.generateAccessToken((Medic) authUser.getPrincipal());
        } else if (user.role().equals("staff")) {
            token = tokenService.generateAccessToken((Staff) authUser.getPrincipal());
        }
        return ResponseEntity.ok(Map.of("token", token, "username", user.username(), "role", user.role()));
    }

}
