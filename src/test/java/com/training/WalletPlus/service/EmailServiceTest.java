package com.training.WalletPlus.service;

import com.training.WalletPlus.dto.EmailDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void testSendSimpleMailSuccess() {
        // Arrange
        EmailDetails details = new EmailDetails("recipient@example.com", "Test message", "Test subject");
        SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
        expectedMailMessage.setTo(details.getRecipient());
        expectedMailMessage.setText(details.getMsgBody());
        expectedMailMessage.setSubject(details.getSubject());

        // Act
        String result = emailService.sendSimpleMail(details);

        // Assert
       verify(javaMailSender).send(expectedMailMessage);
        assertEquals("Mail Sent Successfully...", result);
    }

    @Test
    public void testSendSimpleMailFailure() {
        // Arrange
        EmailDetails details = new EmailDetails("recipient@example.com", "Test message", "Test subject");
        doThrow(new RuntimeException("Mail sending error")).when(javaMailSender).send(any(SimpleMailMessage.class));
        // Act
        String result = emailService.sendSimpleMail(details);

        // Assert
        assertEquals("Error while Sending Mail", result);
    }
    @Test
    public void testSendTransactionAlertEmail() {
        // Mock the JavaMailSender and SimpleMailMessage
//        SimpleMailMessage mailMessage = mock(SimpleMailMessage.class);
//        when(javaMailSender.createMimeMessage()).thenReturn(mailMessage);

        // Test input
        String message = "Recharge Transaction Alert\nuser@example.com\nTransaction details...";

        // Call the method to be tested
        emailService.sendTransactionAlertEmail(message);
        SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
        expectedMailMessage.setTo("user@example.com");
        expectedMailMessage.setText("Transaction details...");
        expectedMailMessage.setSubject("Recharge Transaction Alert");
        // Verify that the email was sent
       // verify(javaMailSender, times(1)).send(mailMessage);
        verify(javaMailSender).send(expectedMailMessage);
        // Verify that the email message details were set correctly
//        verify(mailMessage).setFrom(anyString());
//        verify(mailMessage).setTo("user@example.com");
//        verify(mailMessage).setText("Transaction details...");
//        verify(mailMessage).setSubject("Recharge Transaction Alert");
    }
}
