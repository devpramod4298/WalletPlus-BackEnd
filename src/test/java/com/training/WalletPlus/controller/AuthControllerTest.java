package com.training.WalletPlus.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.training.WalletPlus.config.JwtTokenUtil;
import com.training.WalletPlus.dto.*;
import com.training.WalletPlus.model.User;
import com.training.WalletPlus.repo.UserRepository;
import com.training.WalletPlus.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loginTest_Success() {
        ResponseEntity<Object> entity = new ResponseEntity<>(new LoginResponse("testuser", "test@example.com", "token"), HttpStatus.OK);
        LogInDTO loginRequest = new LogInDTO("testuser", "password123");
        when(userService.login(any(LogInDTO.class))).thenReturn(entity);
        ResponseEntity<Object> responseEntity = authController.login(loginRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void loginTest_BadCredentials() {
        ResponseEntity<Object> entity = new ResponseEntity<>(new ErrorResponse("Bad Credentials"), HttpStatus.BAD_REQUEST);
        LogInDTO loginRequest = new LogInDTO("testuser", "password");
        when(userService.login(any(LogInDTO.class))).thenReturn(entity);
        ResponseEntity<Object> responseEntity = authController.login(loginRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void signupTest_Success() {
        User user=new User("testuser", "test user","user@gmail.com", "password123",0.0,"test Address","9876543210","123456789089");
        ResponseEntity<Object> entity = new ResponseEntity<>(user, HttpStatus.OK);

        when(userService.signup(any(User.class))).thenReturn(entity);

        ResponseEntity<Object> responseEntity = authController.signup(user);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void sendEmailTest()
    {
        ResetPasswordRequest resetPasswordRequest=new ResetPasswordRequest("abc@gmail.com");
        when(userService.sendPasswordRequestEmail(anyString())).thenReturn("");
        ResponseEntity<Object> responseEntity = authController.sendEmailForPassword(resetPasswordRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void resetPasswordComfirmationTest()
    {
        ResponseEntity<Object> entity = new ResponseEntity<>("Password changes successfully!", HttpStatus.OK);
        when(userService.resetpassword(anyString(),anyString())).thenReturn(entity);
        ResponseEntity<Object> responseEntity = authController.resetPassword("token",new PasswordDTO("Abc@123456"));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
