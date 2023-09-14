package com.training.WalletPlus.repo;

import com.training.WalletPlus.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.training.WalletPlus.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testExistsByUserName() {
        // Create test data
        User user = new User("testuser", "Test User", "test@example.com", "password123", 100.0,"test address","9876543210","98765432126");
        userRepository.save(user);
        // Perform the repository call
        boolean exists = userRepository.existsByUserName("testuser");
        // Verify the result
        assertTrue(exists);
    }

    @Test
    public void testExistsByEmailId() {
        // Create test data
        User user = new User("testuser", "Test User", "test@example.com", "password123", 100.0,"test address","9876543210","98765432126");
        userRepository.save(user);
        // Perform the repository call
        boolean exists = userRepository.existsByEmailId("test@example.com");
        // Verify the result
        assertTrue(exists);
    }

    
    @Test
    public void testFindById() {
        // Create test data
        User user = new User("testuser", "Test User", "test@example.com", "password123", 100.0,"test address","9876543210","98765432126");
        userRepository.save(user);
        // Perform the repository call
        Optional<User> foundUser = userRepository.findById("testuser");
        // Verify the result
        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getFullName());
    }
}
