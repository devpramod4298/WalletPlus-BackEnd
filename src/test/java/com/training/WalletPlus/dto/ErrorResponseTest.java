package com.training.WalletPlus.dto;

import com.training.WalletPlus.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseTest {

    @Test
    public void testConstructorAndGetters() {
        String error = "An error occurred";

        ErrorResponse response = new ErrorResponse(error);

        assertEquals(error, response.getError());
    }

    @Test
    public void testEqualsAndHashCode() {
        ErrorResponse response1 = new ErrorResponse("An error occurred");
        ErrorResponse response2 = new ErrorResponse("An error occurred");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    public void testEqualsAndHashCode_DifferentErrors() {
        ErrorResponse response1 = new ErrorResponse("Error 1");
        ErrorResponse response2 = new ErrorResponse("Error 2");

        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    public void testToString() {
        ErrorResponse response = new ErrorResponse("An error occurred");

        String expectedToString = "ErrorResponse(error=An error occurred)";
        assertEquals(expectedToString, response.toString());
    }
}
