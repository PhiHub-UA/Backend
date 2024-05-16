package deti.tqs.phihub.services;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserFromContext() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
    
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
    
        return null;
    }

    public boolean seeIfLoggedIn() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }
    
}
