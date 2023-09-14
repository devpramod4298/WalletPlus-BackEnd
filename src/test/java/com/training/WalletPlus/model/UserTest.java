package com.training.WalletPlus.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testConstructorAndGetters() {
        String userName = "testuser";
        String fullName = "Test User";
        String emailId = "test@example.com";
        String password = "password123";
        double walletAmount = 100.0;

        User user = new User(userName, fullName, emailId, password, walletAmount,"test address","9876543210","98765432112");

        assertEquals(userName, user.getUserName());
        assertEquals(fullName, user.getFullName());
        assertEquals(emailId, user.getEmailId());
        assertEquals(password, user.getPassword());
        assertEquals(walletAmount, user.getWalletAmount());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User("testuser", "Test User", "test@example.com", "password123", 100.0,"","","");
        User user2 = new User("testuser", "Test User", "test@example.com", "password123", 100.0,"","","");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testToString() {
        User user = new User("testuser", "Test User", "test@example.com", "password123", 100.0,"test address","9876543210","987654321098");

        String expectedToString = "User(userName=testuser, fullName=Test User, emailId=test@example.com, password=password123, walletAmount=100.0, address=test address, phoneNumber=9876543210, accountNo=987654321098)";
        assertEquals(expectedToString, user.toString());
    }
}
