package com.training.WalletPlus.service;

import com.training.WalletPlus.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
    public void sendTransactionAlertEmail(String message) {
        String[] lines = message.split("\\n");
        String emailSubject = lines[0];
        String recipientEmail = lines[1];
        String emailBody = message.substring(emailSubject.length() + recipientEmail.length() + 1).trim();
        EmailDetails emailDetails = new EmailDetails(recipientEmail, emailBody, emailSubject);
        sendSimpleMail(emailDetails);
    }
}