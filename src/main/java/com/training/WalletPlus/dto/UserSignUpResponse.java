package com.training.WalletPlus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpResponse {
    private String userName;
    private String fullName;
    private String emailId;
    private String token;
}
