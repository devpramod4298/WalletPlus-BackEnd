package com.training.WalletPlus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
  
	private String username;
	private String emailId;
	private String token;
	
}
