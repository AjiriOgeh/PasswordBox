package com.passwordbox.data.repositories;

import com.passwordbox.data.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRegisters_UserCanBeSaved() {
        User user = new User();
        userRepository.save(user);
        assertEquals(1, userRepository.count());

    }

}