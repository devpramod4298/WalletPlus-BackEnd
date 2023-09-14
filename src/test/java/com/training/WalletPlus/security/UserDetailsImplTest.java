package com.training.WalletPlus.security;

import com.training.WalletPlus.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserDetailsImplTest {

    @Test
    public void testUserDetailsImpl() {
        User user = new User("testuser", "testuser123", "test@example.com", "password123", 0,"test address","9876543210","98765432126");
        UserDetails userDetails = UserDetailsImpl.build(user);

        assertEquals("testuser", userDetails.getUsername());
        assertEquals("test@example.com", ((UserDetailsImpl) userDetails).getEmail());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testAuthorities() {
        UserDetailsImpl userDetails = new UserDetailsImpl("testuser", "testpassword", "test@example.com");
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNull(authorities);
    }
    @Test
    public void testEquals() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl("user1", "pass1", "email1");
        UserDetailsImpl userDetails2 = new UserDetailsImpl("user2", "pass2", "email2");
        UserDetailsImpl userDetails1Duplicate = new UserDetailsImpl("user1", "pass1", "email1");

        assertEquals(userDetails1, userDetails1Duplicate);
        assertNotEquals(userDetails1, userDetails2);
        assertNotEquals(userDetails1, null);
        assertNotEquals(userDetails1, "some string");
    }
    @Test
    public void testEquals_SameObject() {
        UserDetailsImpl userDetails = new UserDetailsImpl("user1", "pass1", "email1");
        assertEquals(userDetails, userDetails);
    }

    @Test
    public void testEquals_EqualObjects() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl("user1", "pass1", "email1");
        UserDetailsImpl userDetails1Duplicate = new UserDetailsImpl("user1", "pass1", "email1");

        assertEquals(userDetails1, userDetails1Duplicate);
    }

    @Test
    public void testEquals_DifferentObjects() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl("user1", "pass1", "email1");
        UserDetailsImpl userDetails2 = new UserDetailsImpl("user2", "pass2", "email2");

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    public void testEquals_Null() {
        UserDetailsImpl userDetails = new UserDetailsImpl("user1", "pass1", "email1");
        assertNotEquals(userDetails, null);
    }

    @Test
    public void testEquals_DifferentType() {
        UserDetailsImpl userDetails = new UserDetailsImpl("user1", "pass1", "email1");
        assertNotEquals(userDetails, "some string");
    }
}

