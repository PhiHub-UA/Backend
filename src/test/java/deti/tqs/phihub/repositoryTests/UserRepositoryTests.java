package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.repositories.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User user0 = new User();
    private User user1 = new User();

    @BeforeAll
    public void setUp() throws Exception {

        user0.setId(1L);
        user0.setEmail("jose@fino.com");
        user0.setPhone("757848656");
        
        userRepository.saveAndFlush(user0);

        user1.setId(2L);
        user1.setEmail("jo@ana.com");
        user1.setPhone("919010828");
    }

    @Test
    void whenFindUserById_thenReturnUser() {

        User found = userRepository.findById(user0.getId()).get();
        
        assertThat(found.getEmail()).isEqualTo(user0.getEmail());
        assertThat(found.getPhone()).isEqualTo(user0.getPhone());
    }

    @Test
    void whenInvalidUserId_thenReturnNull() {
        User found = userRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenUserIsDeleted_thenReturnNull() {
        userRepository.delete(user0);

        User found = userRepository.findById(user0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfUsers_whenFindAll_thenReturnAllUsers() {
        //  Save the user 1
        userRepository.saveAndFlush(user1);

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(2).extracting(User::getEmail).containsOnly(user0.getEmail(), user1.getEmail());
    }
}