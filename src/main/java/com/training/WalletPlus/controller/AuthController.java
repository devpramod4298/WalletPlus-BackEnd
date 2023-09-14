package com.training.WalletPlus.controller;

import com.training.WalletPlus.dto.PasswordDTO;
import com.training.WalletPlus.dto.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.training.WalletPlus.dto.LogInDTO;
import com.training.WalletPlus.model.User;
import com.training.WalletPlus.service.UserService;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userservice;

    @PostMapping("/register")
    public ResponseEntity<Object> signup(@RequestBody User user) {
        return userservice.signup(user);
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LogInDTO loginDTO) {
        return userservice.login(loginDTO);
    }
    @PostMapping("/password-reset-request")
    public ResponseEntity<Object> sendEmailForPassword(@RequestBody ResetPasswordRequest request) {
        String emailId = request.getEmailId();
        userservice.sendPasswordRequestEmail(emailId);
        return ResponseEntity.ok("Password reset email sent successfully!");
    }
    @PostMapping("/password-reset-confirmation")
    public ResponseEntity<Object> resetPassword(@RequestHeader("Authorization") String token, @RequestBody PasswordDTO passwordDto) {
        return userservice.resetpassword(token, passwordDto.getPassword());
    }
}