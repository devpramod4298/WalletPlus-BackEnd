package com.training.WalletPlus.service;
import com.training.WalletPlus.dto.EmailDetails;
import com.training.WalletPlus.dto.ErrorResponse;
import com.training.WalletPlus.dto.LogInDTO;
import com.training.WalletPlus.dto.LoginResponse;
import com.training.WalletPlus.model.User;
import com.training.WalletPlus.repo.UserRepository;
import com.training.WalletPlus.security.UserDetailsImpl;
import com.training.WalletPlus.config.JwtTokenUtil;
import com.training.WalletPlus.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void signupTest_WhenUsernameIsAlreadyTaken()
    {
        User user=  new User("john","John Bist","johnbist@gmail.com","john1234",0.0,"test address","9876543210","98765432126");

        when(userRepo.existsById(any())).thenReturn(true);
        ResponseEntity<Object> entity= userService.signup(user);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,entity.getStatusCode());
        verify(userRepo,times(0)).save(any());
    }
    @Test
    public void signupTest_WhenUsernameIsNotTaken()
    {
        User user=  new User("john","John Bist","johnbist@gmail.com","john1234",0.0,"test address","9876543210","98765432126");
        when(userRepo.existsById(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("john1234");
        when(userRepo.save(any())).thenReturn(user);
        ResponseEntity<Object> entity= userService.signup(user);
        Assertions.assertEquals(HttpStatus.OK,entity.getStatusCode());
        verify(userRepo,times(1)).save(any());
    }


    @Test
    public void signupTest_WhenEmailIsAlreadyTaken(){
        User user=  new User("john","John Bist","johnbist@gmail.com","john1234",0.0,"test address","9876543210","98765432126");

        when(userRepo.existsByEmailId(any())).thenReturn(true);
        ResponseEntity<Object> entity= userService.signup(user);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,entity.getStatusCode());
        verify(userRepository,times(0)).save(any());
    }

    @Test
    public void signupTest_WhenEmailIsNotTaken(){
      User user=  new User("john","John Bist","johnbist@gmail.com","john1234",0.0,"test address","9876543210","98765432126");
      when(userRepo.existsByEmailId(any())).thenReturn(false);
      when(userRepo.existsById(any())).thenReturn(false);
      when(passwordEncoder.encode(any())).thenReturn("john1234");
      when(userRepo.save(any())).thenReturn(user);
        ResponseEntity<Object> entity= userService.signup(user);
        assertEquals(HttpStatus.OK,entity.getStatusCode());
        verify(userRepo,times(1)).save(any());
    }

    @Test
    public void signupTest_Exception(){
        User user=  new User("john","John Bist","johnbist@gmail.com","john1234",0.0,"test address","9876543210","98765432126");
        when(userRepo.save(any())).thenThrow(new RuntimeException("Error in saving the user"));
        ResponseEntity<Object> responseEntity = userService.signup(user);
       assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }
    @Test
    public void signupTest_Exception2(){
        User user=  new User("john","John Bist","johnbist@gmail.com","john1234",0.0,"test address","9876543210","98765432126");
        when(userRepo.save(any())).thenThrow(new Error("Error in saving the user"));
        Assertions.assertThrows(Error.class,()->userService.signup(user)) ;
    }
    @Test
    public void testLogin() {

        LogInDTO loginDTO = new LogInDTO("testuser", "testpassword");
        UserDetailsImpl userDetails = new UserDetailsImpl("testuser", "testpassword", "test@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtTokenUtil.generateTokenFromUsername("testuser")).thenReturn("testToken");

        ResponseEntity<Object> responseEntity = userService.login(loginDTO);

        assertEquals("testuser", ((LoginResponse) responseEntity.getBody()).getUsername());
        assertEquals("test@example.com", ((LoginResponse) responseEntity.getBody()).getEmailId());
        assertEquals("testToken", ((LoginResponse) responseEntity.getBody()).getToken());
    }
    @Test
    public void testSendPasswordRequestEmail() {
        // Arrange
        String emailId = "test@example.com";
        User user = new User();
        user.setUserName("testUser");
        user.setFullName("Test User");

        when(userRepo.findByEmailId(emailId)).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateTokenFromUsername(user.getUserName())).thenReturn("testToken");

        ArgumentCaptor<EmailDetails> emailDetailsCaptor = ArgumentCaptor.forClass(EmailDetails.class);

        // Act
        String result = userService.sendPasswordRequestEmail(emailId);

        // Assert
        verify(emailService).sendSimpleMail(emailDetailsCaptor.capture());
        EmailDetails emailDetails = emailDetailsCaptor.getValue();

        assertEquals("Mail Sent Successfully", result);
        assertEquals(emailId, emailDetails.getRecipient());
        assertTrue(emailDetails.getMsgBody().contains("Dear Test User"));
        assertTrue(emailDetails.getSubject().contains("Password Change Request Confirmation"));
    }
    @Test
    public void testResetPasswordSuccess() {
        // Arrange
        String token = "testToken";
        String password = "newPassword";
        User user = new User();
        user.setUserName("testUser");

        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn(user.getUserName());
        when(userRepo.findById(user.getUserName())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Object> responseEntity = userService.resetpassword(token, password);

        // Assert
        verify(passwordEncoder).encode(password);
        verify(userRepo).save(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Password changes successfully!", responseEntity.getBody());
    }
    @Test
    public void testResetPasswordFailure() {
        // Arrange
        String token = "testToken";
        String password = "newPassword";

        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn("nonExistentUser");
        when(userRepo.findById(any())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = userService.resetpassword(token, password);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("Something went wrong, Please try again!", errorResponse.getError());
    }
}
