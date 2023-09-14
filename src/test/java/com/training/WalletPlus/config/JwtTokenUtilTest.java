package com.training.WalletPlus.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiry", 864000000);
    }

    @Test
    public void testGenerateTokenFromUsername() {
        String token = jwtTokenUtil.generateTokenFromUsername("testuser");

        assertNotNull(token);
    }

    @Test
    public void testGetUsernameFromToken() {
        String token = jwtTokenUtil.generateTokenFromUsername("testuser");
        String username = jwtTokenUtil.getUsernameFromToken(token);

        assertEquals("testuser", username);
    }

    @Test
    public void testValidateToken_ValidToken() {
        String token = generateValidToken();

        assertTrue(jwtTokenUtil.validateToken(token));
    }

//    @Test
//    public void testValidateToken_InvalidToken() {
//        String token = "invalidToken";
//
//        assertFalse(jwtTokenUtil.validateToken(token));
//    }

    private String generateValidToken() {
        return Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 864000000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();
    }
}
