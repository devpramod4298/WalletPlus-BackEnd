package com.training.WalletPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import com.training.WalletPlus.dto.TransferRequestDTO;
import com.training.WalletPlus.dto.WalletAmount;
import com.training.WalletPlus.service.WalletService;

@CrossOrigin("*")
@RestController
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
	WalletService walletService;

	@GetMapping("/all-transactions")
	public ResponseEntity<Object> showTransactions(@RequestHeader("Authorization") String token ,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
		Pageable paging = PageRequest.of(page, size);

		return walletService.showAllTransactions(token,paging);
	}

	@PostMapping("/recharge")
	public ResponseEntity<Object> recharge(@RequestHeader("Authorization") String token,
			@RequestBody WalletAmount walletAmount) {
		return walletService.walletRecharge(token, walletAmount.getAmount());
	}

	@PostMapping("/transfer")
	public ResponseEntity<Object> walletTransfer(@RequestHeader("Authorization") String token,
			@RequestBody TransferRequestDTO transferrequestDTO) {
		return walletService.walletAmountTransfer(token, transferrequestDTO);
	}
	@GetMapping("/show-balance")
	public ResponseEntity<Object> showBalance(@RequestHeader("Authorization") String token) {
		return walletService.showBalance(token);
	}
}