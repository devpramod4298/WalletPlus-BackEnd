package com.training.WalletPlus.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LogInDTOTest {

    @Test
    public void testConstructorAndGetters() {
        String userName = "testuser";
        String password = "password123";

        LogInDTO loginDTO = new LogInDTO(userName, password);

        assertEquals(userName, loginDTO.getUserName());
        assertEquals(password, loginDTO.getPassword());
    }

    @Test
    public void testEqualsAndHashCode() {
        LogInDTO dto1 = new LogInDTO("testuser", "password123");
        LogInDTO dto2 = new LogInDTO("testuser", "password123");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_DifferentValues() {
        LogInDTO dto1 = new LogInDTO("testuser", "password123");
        LogInDTO dto2 = new LogInDTO("testuser", "differentPassword");

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testToString() {
        LogInDTO loginDTO = new LogInDTO("testuser", "password123");

        String expectedToString = "LogInDTO(userName=testuser, password=password123)";
        assertEquals(expectedToString, loginDTO.toString());
    }
}
