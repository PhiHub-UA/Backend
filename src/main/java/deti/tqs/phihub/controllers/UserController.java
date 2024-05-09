package deti.tqs.phihub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import deti.tqs.phihub.models.User;

import deti.tqs.phihub.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUser(HttpServletRequest request) {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return ResponseEntity.status(401).build();
        }
        
        var user = userService.getUserFromContext();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id, HttpServletRequest request) {
        var loggedInUser = userService.getUserFromContext();
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (loggedInUser.getRole().equals("admin")) {
            return ResponseEntity.ok(user);
        }

        // a user trying to check another user's info, aint no way
        if (!loggedInUser.getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user);
    }
    
}
