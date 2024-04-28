package deti.tqs.phihub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;

import deti.tqs.phihub.models.User;

import deti.tqs.phihub.services.UserService;
import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
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
