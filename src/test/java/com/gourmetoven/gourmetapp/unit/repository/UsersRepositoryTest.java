package com.gourmetoven.gourmetapp.unit.repository;

import com.gourmetoven.gourmetapp.entity.Users;
import com.gourmetoven.gourmetapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UsersRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindUserByName() {

        // Create a new user and save it to the repository
        Users user = new Users();
        user.setUsername("user10");
        user.setName("John");

        userRepository.save(user);

        // Find the user by username
        Optional<Users> foundUser = userRepository.findByUsername("user10");

        // Check that the user was found and has the correct name
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John");
    }

    @Test
    public void shouldNotFindUserByName() {
        // Try to find a user that doesn't exist

        Optional<Users> foundUser = userRepository.findByUsername("not_exist_john");

        // Check that the user was not found

        assertThat(foundUser).isNotPresent();
    }
}
