package deti.tqs.phihub.services;

import deti.tqs.phihub.dtos.RegisterSchema;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Staff;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.repositories.MedicRepository;
import deti.tqs.phihub.repositories.StaffRepository;
import deti.tqs.phihub.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

    private UserRepository userRepository;
    private MedicRepository medicRepository;
    private StaffRepository staffRepository;

    public AuthService(UserRepository userRepository, MedicRepository medicRepository,
            StaffRepository staffRepository) {
        this.userRepository = userRepository;
        this.medicRepository = medicRepository;
        this.staffRepository = staffRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        Medic medic = medicRepository.findByUsername(username);
        Staff staff = staffRepository.findByUsername(username);

        if (user != null) {
            return user;
        }

        if (medic != null) {
            return medic;
        }

        if (staff != null) {
            return staff;
        }

        throw new UsernameNotFoundException("User not found");
    }

    public UserDetails registerUser(RegisterSchema user) throws AuthenticationException {

        if (userRepository.findByUsername(user.username()) != null) {
            System.out.println("----------------------------------------------------------------------------------same user name" + user.username());
            return null;
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());

        UserDetails registeredUser = null;

        if (user.role().equals("user")) {

            User newUser = new User(user.name(), user.phone(), user.email(), user.age(), user.username(),
                    encryptedPassword);
            userRepository.save(newUser);
            registeredUser = newUser;
        }

        if (user.role().equals("medic")) {
            Medic newMedic = new Medic(user.name(), user.phone(), user.email(), user.age(), user.username(),
                    encryptedPassword);
            medicRepository.save(newMedic);
            registeredUser = newMedic;
        }

        if (user.role().equals("staff")) {
            Staff newStaff = new Staff(user.name(), user.phone(), user.email(), user.age(), user.username(),
                    encryptedPassword);
            staffRepository.save(newStaff);
            registeredUser = newStaff;
        }

        return registeredUser;

    }
}
