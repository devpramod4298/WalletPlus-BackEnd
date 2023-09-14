package com.training.WalletPlus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "User")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
	@Id
	private String userName;
	private String fullName;
	private String emailId;
	private String password;
	@JsonInclude(Include.NON_NULL)
	private double walletAmount;
	private String address;
	private String phoneNumber;
	private String accountNo;
}
