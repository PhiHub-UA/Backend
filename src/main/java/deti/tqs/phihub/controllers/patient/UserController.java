package deti.tqs.phihub.controllers.patient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.services.StaffService;
import deti.tqs.phihub.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/patient/users")
public class UserController {

    private UserService userService;

    private MedicService medicService;

    private StaffService staffService;

    public UserController(UserService userService, MedicService medicService, StaffService staffService) {
        this.userService = userService;
        this.medicService = medicService;
        this.staffService = staffService;
    }

    @Operation(summary = "Get logged in user", description = "Get information of logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUser(HttpServletRequest request) {

        var user = userService.getUserFromContext();

        if (user == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get user", description = "Get information of a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id, HttpServletRequest request) {

        var loggedInUser = userService.getUserFromContext();

        User user = userService.getUserById(id);

        if (user == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found");
        }

        boolean canCheck = false;


        if (medicService.getMedicFromContext() != null) {
            canCheck = true;
        }

        if (loggedInUser != null && loggedInUser.getUsername().equals(user.getUsername())) {
            canCheck = true;
        }

        if (staffService.getStaffFromContext() != null) {
            canCheck = true;
        }

        if (!canCheck) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ResponseEntity.ok(user);
    }

}
