package deti.tqs.phihub.defaults;

import org.springframework.boot.ApplicationRunner;

import deti.tqs.phihub.configs.Generated;
import deti.tqs.phihub.repositories.UserRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;


import deti.tqs.phihub.models.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
@Generated
public class Users implements ApplicationRunner {

    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (userRepository.count() > 0) {
            return;
        }

        User mariadiabetica = new User("Maria Diabética", "98765431", "mariadiabetes@gmail.com", 45, "mariadiabetes",
                bCryptPasswordEncoder.encode("mariadiabetes"));

        User joaotenso = new User("João Tenso", "98765431", "joaotenso@gmail.com", 23, "joaotenso",
                bCryptPasswordEncoder.encode("joaotenso"));

        userRepository.save(mariadiabetica);
        userRepository.save(joaotenso);

    }
}
