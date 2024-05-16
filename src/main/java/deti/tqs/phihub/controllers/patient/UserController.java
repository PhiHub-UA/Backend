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

    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUser(HttpServletRequest request) {

        var user = userService.getUserFromContext();

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id, HttpServletRequest request) {

        var loggedInUser = userService.getUserFromContext();

        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
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
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user);
    }

}
