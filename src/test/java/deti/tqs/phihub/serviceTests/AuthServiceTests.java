package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.UserRepository;
import deti.tqs.phihub.services.AuthService;
import deti.tqs.phihub.dtos.RegisterSchema;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user0 = new User();
    private RegisterSchema user0Schema;

    @BeforeEach
    public void setUp() {

        user0.setId(1L);
        user0.setName("Josefino Calças");
        user0.setUsername("josecalças");
        user0.setEmail("jose@fino.com");
        user0.setPhone("929838747");

        user0Schema = new RegisterSchema(user0.getName(), user0.getPhone(), user0.getEmail(), 0, user0.getUsername(), "", "");

        Mockito.when(userRepository.findByUsername(user0.getUsername())).thenReturn(null);
        Mockito.when(userRepository.save(user0)).thenReturn(user0);
    }

    @Test
     void whenRegisterNewUser_thenReturnNewUserDetails() {
        UserDetails returned = authService.registerUser(user0Schema);
        assertThat(returned.getUsername()).isEqualTo(user0.getUsername());

        Mockito.verify(userRepository, 
                VerificationModeFactory.times(1))
                    .findByUsername(user0.getUsername());
    }

    @Test
     void whenRegisterExistingUser_thenReturnExistsError() {
        //  Simulate username already existing
        Mockito.when(userRepository.findByUsername(user0.getUsername())).thenReturn(user0);

        assertThatThrownBy(() -> authService.registerUser(user0Schema)).isInstanceOf(IllegalArgumentException.class);

        Mockito.verify(userRepository, 
                VerificationModeFactory.times(0))
                    .save(Mockito.any());
    }

    @Test
     void whenSearchValidUserName_thenUserShouldBeFound() {
        //  Simulate username already existing
        Mockito.when(userRepository.findByUsername(user0.getUsername())).thenReturn(user0);

        UserDetails found = authService.loadUserByUsername(user0.getUsername());
        assertThat(found.getUsername()).isEqualTo(user0.getUsername());
    }

    @Test
     void whenSearchInvalidUserName_thenErrorShouldBeFound() {
        assertThatThrownBy(() -> authService.loadUserByUsername(user0.getUsername())).isInstanceOf(UsernameNotFoundException.class);
    }
}
