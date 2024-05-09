package deti.tqs.phihub.services;

import deti.tqs.phihub.dtos.RegisterSchema;
import deti.tqs.phihub.models.User;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.repositories.UserRepository;


@Service
public class AuthService implements UserDetailsService {

    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public UserDetails registerUser(RegisterSchema user) throws AuthenticationException {
        if (userRepository.findByUsername(user.username()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User newUser = new User(user.phone(),user.email(),user.age(),user.username(),encryptedPassword,"user");
        userRepository.save(newUser);
        return newUser;
    }
}
