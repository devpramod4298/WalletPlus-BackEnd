package com.training.WalletPlus.service;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.training.WalletPlus.dto.TransactionListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.WalletPlus.dto.TransferRequestDTO;
import com.training.WalletPlus.model.Transaction;
import com.training.WalletPlus.model.User;
import com.training.WalletPlus.repo.TransactionRepository;
import com.training.WalletPlus.repo.UserRepository;
import com.training.WalletPlus.config.JwtTokenUtil;

@Service
public class WalletService {

	private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

	@Autowired
	UserRepository userRepository;
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	@Transactional
	public ResponseEntity<Object> walletRecharge(String token, double amount) {
		logger.info("Executing walletRecharge");
		Optional<User> optionalSavedUser = userRepository.findById(jwtTokenUtil.getUsernameFromToken(token));
		if (optionalSavedUser.isPresent()) {
			User savedUser = optionalSavedUser.get();
			String transactionId = String.valueOf(System.currentTimeMillis());

			Transaction transaction = new Transaction(transactionId, "wallet-recharge", "Bank", savedUser.getUserName(),
					amount, new Date());
			double currentAmount = savedUser.getWalletAmount() + amount;
			savedUser.setWalletAmount(currentAmount);

			userRepository.save(savedUser);
			transactionRepository.save(transaction);
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			Date currentDateTime = new Date();
			String formattedDateTime = dateTimeFormat.format(currentDateTime);
			logger.info("Recharged Successfully");
			String rechargeMessage = "Recharge Transaction Alert\n" + savedUser.getEmailId() + "\n" + "Dear "
					+ savedUser.getFullName() + ",\n" + "\n"
					+ "We hope this email finds you well. We are writing to inform you about a recent transaction on your Wallet Plus account.\n"
					+ "\n" + "Your Wallet Plus account has been credited with an amount of " + amount
					+ ". This transaction was initiated by you to recharge your wallet.\n" + "\n"
					+ "For your reference, here are some important points regarding this transaction:\n" + "\n"
					+ "1. Transaction ID: " + transactionId
					+ " - You can use this ID to track the transaction if needed.\n"
					+ "2. Transaction Type: Wallet Recharge - This indicates that funds were added to your wallet.\n"
					+ "3. Amount: " + amount + " - The amount that has been credited to your wallet.\n"
					+ "4. Date and Time: "+formattedDateTime+" - The date and time when the transaction was processed.\n"
					+ "\n" + "You can check your wallet balance anytime by logging into your Wallet Plus account.\n"
					+ "\n"
					+ "If you did not initiate this transaction or have any concerns about it, please contact our support team immediately at [Support Email Address]. Your security is our priority, and we will investigate any unauthorized activity.\n"
					+ "\n"
					+ "Thank you for choosing Wallet Plus for your financial needs. If you have any questions or require further assistance, please do not hesitate to reach out to our customer support team.\n"
					+ "\n" + "Sincerely,\n" + "\n" + "[Wallet Plus]\n" + "Customer Support Team\n"
					+ "[support@walletplus.com]";
			kafkaTemplate.send("transaction-alerts", rechargeMessage);
			return ResponseEntity.ok("Recharged Successfully");
		} else {
			logger.warn("user not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}
	
	@Transactional
	public ResponseEntity<Object> walletAmountTransfer(String token, TransferRequestDTO transferrequestDTO) {
		logger.info("executing walletAmountTransfer..");
		Optional<User> optionalSender = userRepository.findById(jwtTokenUtil.getUsernameFromToken(token));

		if (!optionalSender.isPresent()) {
			logger.warn("Sender not found");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender not found");
		}

		User sender = optionalSender.get();
		String transactionId = String.valueOf(System.currentTimeMillis());

		Optional<User> optionalReceiver = userRepository.findById(transferrequestDTO.getReciver());

		if (!optionalReceiver.isPresent()) {
			logger.warn("Receiver not found");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receiver not found");
		}
		User receiver = optionalReceiver.get();
		double currentAmount = sender.getWalletAmount();
		double transferAmount = transferrequestDTO.getTranferamount();

		if (Double.compare(currentAmount, transferAmount) < 0) {
			logger.warn("Fund not available");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fund Not Available");
		}
		Transaction transaction = new Transaction(transactionId, "transfer", sender.getUserName(), receiver.getUserName(),
				transferAmount, new Date());
		transactionRepository.save(transaction);
		sender.setWalletAmount(currentAmount - transferAmount);
		userRepository.save(sender);
		receiver.setWalletAmount(receiver.getWalletAmount() + transferAmount);
		userRepository.save(receiver);
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Date currentDateTime = new Date();
		String formattedDateTime = dateTimeFormat.format(currentDateTime);

		String messageForSender="Fund Transfer Confirmation\n" +
				sender.getEmailId()+"\n" +
				"Dear "+sender.getFullName()+",\n" +
				"\n" +
				"We're confirming your recent fund transfer:\n" +
				"\n" +
				"    Transaction ID: "+transactionId +"\n"+
				"    Recipient: "+receiver.getFullName()+"\n" +
				"    Amount: "+transferAmount+"\n" +
				"    Date: "+formattedDateTime+"\n" +
				"\n" +
				"Thank you for choosing Wallet Plus for your financial needs. If you have any questions or require further assistance, please do not hesitate to reach out to our customer support team.\n"
				+ "\n" + "Sincerely,\n" + "\n" + "[Wallet Plus]\n" + "Customer Support Team\n"
				+ "[support@walletplus.com]";
		kafkaTemplate.send("transaction-alerts", messageForSender);
		String msgForreceiver="Fund Transfer Received\n" +
				receiver.getEmailId()+"\n" +
				"Dear "+receiver.getFullName()+",\n" +
				"\n" +
				"You've received a fund transfer:\n" +
				"\n" +
				"    Transaction ID: "+transactionId +"\n"+
				"   Sender: "+sender.getFullName()+"\n" +
				"    Amount: "+transferAmount+"\n" +
				"    Date: "+formattedDateTime+"\n" +
				"\n" +
				"Thank you for choosing Wallet Plus for your financial needs. If you have any questions or require further assistance, please do not hesitate to reach out to our customer support team.\n"
				+ "\n" + "Sincerely,\n" + "\n" + "[Wallet Plus]\n" + "Customer Support Team\n"
				+ "[support@walletplus.com]";
		kafkaTemplate.send("transaction-alerts", msgForreceiver);

		logger.info("Transaction Successful");
		return ResponseEntity.ok("Transaction Successful");
	}

	public ResponseEntity<Object> showBalance(String token) {
		logger.info("Executing showBalance");
		String currentUserName = jwtTokenUtil.getUsernameFromToken(token);
		return new ResponseEntity<>((userRepository.findById(currentUserName)).get(), HttpStatus.OK);
	}
	public ResponseEntity<Object> showAllTransactions(String token, Pageable paging) {
		logger.info("Executing showAllTransaction..");
		String currentUserName = jwtTokenUtil.getUsernameFromToken(token);
		Page<Transaction> pageTransactions = transactionRepository.findByReceiverOrSender(currentUserName,paging);

		List<Transaction> allTransactions = pageTransactions.getContent();

//		 Replace the currentUserName with "self" in the transaction list 
		List<Transaction> finalList = allTransactions.stream()
				.peek(transaction -> {
					if (transaction.getReceiver().equals(currentUserName)) {
						transaction.setReceiver("self");
					}
					if (transaction.getSender().equals(currentUserName)) {
						transaction.setSender("self");
						transaction.setAmount(-transaction.getAmount());
					}
				})
				.sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
				.collect(Collectors.toList());
		TransactionListDTO transactionListDTO=new TransactionListDTO(finalList,pageTransactions.getNumber(),pageTransactions.getTotalElements(),pageTransactions.getTotalPages());
		logger.info("All transactions List: {}", transactionListDTO);
		return new ResponseEntity<>(transactionListDTO, HttpStatus.OK);
	}

}
