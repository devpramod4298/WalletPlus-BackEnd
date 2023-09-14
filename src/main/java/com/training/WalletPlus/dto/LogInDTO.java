package com.training.WalletPlus.dto;

import lombok.*;
import org.springframework.context.annotation.Bean;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogInDTO {

	private String userName;
	private String password;
}