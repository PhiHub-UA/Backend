package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User registerUser(User user) {

        User newUser = userRepository.save(user);

        return newUser;

    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
