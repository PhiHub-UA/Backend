package deti.tqs.phihub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.models.User;

import deti.tqs.phihub.services.UserService;
import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;

    private TokenProvider tokenProvider;

    @Autowired
    public UserController(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id, HttpServletRequest request) {

        String access_token = request.getHeader("Authorization").replace("Bearer ", "");

        if (access_token == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String loggedinUser = tokenProvider.validateToken(access_token);

        // admin can check all users, TODO 
        
        if (user.getRole().equals("admin")) {
            return ResponseEntity.ok(user);
        }

        // a user trying to check another user's info, aint no way
        if (!loggedinUser.equals(user.getUsername())) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user);
    }
    
}
