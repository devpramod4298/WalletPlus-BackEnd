package com.training.WalletPlus.model;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Document("Transaction")
@Data
@AllArgsConstructor
public class Transaction {
	@Id
	private String transactionId;
	private String transactionType;
	private String sender;
	private String receiver;
	private double amount;
	private Date transactionDate;
}
