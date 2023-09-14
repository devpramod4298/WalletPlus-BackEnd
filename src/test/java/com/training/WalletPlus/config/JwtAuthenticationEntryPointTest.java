package com.training.WalletPlus.config;

import com.training.WalletPlus.config.JwtAuthenticationEntryPoint;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationEntryPointTest {

    @Test
    public void testCommence() throws IOException, ServletException {
        AuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Unauthorized") {};

        entryPoint.commence(request, response, authException);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        assertTrue(response.getContentAsString().contains("Unauthorized"));
        assertTrue(response.getContentAsString().contains("Unauthorized"));
        assertTrue(response.getContentAsString().contains(authException.getMessage()));
    }
}
