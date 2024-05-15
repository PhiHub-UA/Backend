package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.MedicRepository;
import deti.tqs.phihub.repositories.StaffRepository;
import deti.tqs.phihub.repositories.UserRepository;
import deti.tqs.phihub.services.AuthService;
import deti.tqs.phihub.dtos.RegisterSchema;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private UserRepository userRepository;

    @Mock(strictness = Strictness.LENIENT)
    private MedicRepository medicRepository;

    @Mock(strictness = Strictness.LENIENT)
    private StaffRepository staffRepository;

    @InjectMocks
    private AuthService authService;

    private User user0 = new User();
    private User user1 = new User();
    private User user2 = new User();
    private RegisterSchema user0Schema;
    private RegisterSchema user1Schema;
    private RegisterSchema user2Schema;

    @BeforeEach
    public void setUp() {

        //  Regular user
        user0.setId(1L);
        user0.setUsername("josecalças");
        user0.setEmail("jose@fino.com");
        user0.setPhone("929838747");
        user0Schema = new RegisterSchema(user0.getPhone(), user0.getEmail(), 0, user0.getUsername(), "jos321", "user", "jos");

        //  Medic user
        user1.setId(2L);
        user1.setUsername("tomaspires");
        user1.setEmail("tom@pir.com");
        user1.setPhone("929838747");
        user1Schema = new RegisterSchema(user1.getPhone(), user1.getEmail(), 0, user1.getUsername(), "tom123", "medic", "tom");

        //  Staff user
        user2.setId(3L);
        user2.setUsername("rodigosaia");
        user2.setEmail("rod@saia.com");
        user2.setPhone("929838747");
        user2Schema = new RegisterSchema(user2.getPhone(), user2.getEmail(), 0, user2.getUsername(), "rod112233", "staff", "rod");

        Mockito.when(userRepository.save(user0)).thenReturn(user0);
        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        Mockito.when(userRepository.save(user2)).thenReturn(user2);
    }

    @Test
    void whenRegisterNewUser_thenReturnNewUserDetails() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        //  Regular User
        UserDetails returned = authService.registerUser(user0Schema);
        assertThat(returned.getUsername()).isEqualTo(user0.getUsername());

        Mockito.verify(userRepository,
                VerificationModeFactory.times(1))
                .findByUsername(user0.getUsername());

        //  Medic User
        returned = authService.registerUser(user1Schema);
        assertThat(returned.getUsername()).isEqualTo(user1.getUsername());

        Mockito.verify(userRepository,
                VerificationModeFactory.times(1))
                .findByUsername(user1.getUsername());

        //  Staff User
        returned = authService.registerUser(user2Schema);
        assertThat(returned.getUsername()).isEqualTo(user2.getUsername());

        Mockito.verify(userRepository,
                VerificationModeFactory.times(1))
                .findByUsername(user2.getUsername());
    }

    @Test
    void whenRegisterExistingUser_thenReturnExistsError() {
        // Simulate username already existing
        Mockito.when(userRepository.findByUsername(user0.getUsername())).thenReturn(user0);
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(user2);

        assertThat(authService.registerUser(user0Schema)).isNull();
        assertThat(authService.registerUser(user1Schema)).isNull();
        assertThat(authService.registerUser(user2Schema)).isNull();

        Mockito.verify(userRepository,
                VerificationModeFactory.times(0))
                .save(Mockito.any());
    }

    @Test
    void whenSearchValidUserName_thenUserShouldBeFound() {
        // Simulate username already existing
        Mockito.when(userRepository.findByUsername(user0.getUsername())).thenReturn(user0);
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(user2);

        UserDetails found = authService.loadUserByUsername(user0.getUsername());
        assertThat(found.getUsername()).isEqualTo(user0.getUsername());

        found = authService.loadUserByUsername(user1.getUsername());
        assertThat(found.getUsername()).isEqualTo(user1.getUsername());
        
        found = authService.loadUserByUsername(user2.getUsername());
        assertThat(found.getUsername()).isEqualTo(user2.getUsername());
    }

    @Test
    void whenSearchInvalidUserName_thenErrorShouldBeFound() {
        String uname = user0.getUsername();

         assertThatThrownBy(() ->
        authService.loadUserByUsername(uname)).isInstanceOf(UsernameNotFoundException.class);
    }
}
