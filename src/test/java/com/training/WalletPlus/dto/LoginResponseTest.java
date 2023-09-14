package com.training.WalletPlus.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseTest {

    @Test
    public void testConstructorAndGetters() {
        String username = "testuser";
        String emailId = "test@example.com";
        String token = "testToken";

        LoginResponse response = new LoginResponse(username, emailId, token);

        assertEquals(username, response.getUsername());
        assertEquals(emailId, response.getEmailId());
        assertEquals(token, response.getToken());
    }

    @Test
    public void testEqualsAndHashCode() {
        LoginResponse response1 = new LoginResponse("testuser", "test@example.com", "testToken");
        LoginResponse response2 = new LoginResponse("testuser", "test@example.com", "testToken");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_DifferentValues() {
        LoginResponse response1 = new LoginResponse("testuser", "test@example.com", "testToken");
        LoginResponse response2 = new LoginResponse("testuser", "different@example.com", "testToken");

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    public void testToString() {
        LoginResponse response = new LoginResponse("testuser", "test@example.com", "testToken");

        String expectedToString = "LoginResponse(username=testuser, emailId=test@example.com, token=testToken)";
        assertEquals(expectedToString, response.toString());
    }
}
