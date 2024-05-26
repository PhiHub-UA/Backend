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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.UserRepository;
import deti.tqs.phihub.services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user0 = new User();
    private User user1 = new User();

    @BeforeEach
    public void setUp() {
        //  Create two users
        user0.setId(1L);
        user0.setUsername("Josefino");
        user0.setAge(39);

        user1.setId(2L);
        user1.setUsername("Joana");
        user1.setAge(27);

        List<User> allUsers = Arrays.asList(user0, user1);

        Mockito.when(userRepository.save(user0)).thenReturn(user0);
        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
        Mockito.when(userRepository.findById(user0.getId())).thenReturn(Optional.of(user0));
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById(-99L)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user0.getUsername())).thenReturn(user0);
    }

    @Test
     void whenSaveValidUser_thenUserShouldBeReturned() {
        User returned = userService.save(user0);
        assertThat(returned.getUsername()).isEqualTo(user0.getUsername());

        returned = userService.save(user1);
        assertThat(returned.getUsername()).isEqualTo(user1.getUsername());
    }

    @Test
     void whenSearchValidID_thenUsershouldBeFound() {
        User found = userService.getUserById(user0.getId());
        assertThat(found.getUsername()).isEqualTo(user0.getUsername());

        found = userService.getUserById(user1.getId());
        assertThat(found.getUsername()).isEqualTo(user1.getUsername());
    }

    @Test
     void whenSearchInvalidID_thenUserShouldNotBeFound() {
        User fromDb = userService.getUserById(-99L);
        assertThat(fromDb).isNull();

        Mockito.verify(userRepository, 
                VerificationModeFactory.times(1))
                    .findById(-99L);
    }

    @Test
     void whenSearchValidUserName_thenUserShouldBeFound() {
        User found = userService.findByUsername(user0.getUsername());
        assertThat(found.getUsername()).isEqualTo(user0.getUsername());
    }

    @Test
     void whenUserContext_thenUserShouldBeLoggedIn() {
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Boolean loggedIn = userService.seeIfLoggedIn();
        assertThat(loggedIn).isTrue();
    }

    @Test
     void whenNoUserContext_thenUserShouldNotBeLoggedIn() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThat(userService.seeIfLoggedIn()).isFalse();
    }

    @Test
     void whenUserContext_thenUserShouldBeFound() {
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getPrincipal()).thenReturn(user0);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertThat(userService.getUserFromContext().getUsername()).isEqualTo(user0.getUsername());
    }

    @Test
     void whenUserContext_ButBadAuththenNoUserShouldNotBeFound() {
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getPrincipal()).thenReturn(null);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertThat(userService.getUserFromContext()).isNull();
    }

    @Test
     void whenNoUserContext_thenUserShouldNotBeFound() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThat(userService.getUserFromContext()).isNull();
    }
}
