package com.training.WalletPlus.service;

import com.training.WalletPlus.config.JwtTokenUtil;
import com.training.WalletPlus.dto.TransactionListDTO;
import com.training.WalletPlus.dto.TransferRequestDTO;
import com.training.WalletPlus.model.Transaction;
import com.training.WalletPlus.model.User;
import com.training.WalletPlus.repo.TransactionRepository;
import com.training.WalletPlus.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WalletServiceTest
{
    @Mock
    UserRepository userRepository;
    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    private WalletService walletService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private JwtTokenUtil jwtUtil;
    @Test
    public void walletRechargeTest()
    {
        User savedUser = new User("abc","abc","abc@gmail.com","abc123",1000,"test address","9876543210","98765432126");
       // WalletRechargeRequest rechargeRequest = new WalletRechargeRequest(100);
        User updatedUser = new User("abc","abc","abc@gmail.com","abc123",1100,"test address","9876543210","98765432126");

        when(userRepository.findById(any())).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any())).thenReturn(updatedUser);
        when(transactionRepository.save(any())).thenReturn(new Transaction("","wallet-recharge","Bank","abc",100,new Date()));
        when(kafkaTemplate.send(anyString(),anyString())).thenReturn(null);
        ResponseEntity<Object> responseEntity = walletService.walletRecharge(anyString(),100);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        verify(transactionRepository,times(1)).save(any());
        verify(userRepository,times(1)).save(any());
    }
    @Test
    public void walletRechargeTestWithInvalidUser()
    {
        User savedUser = new User("abc","abc","abc@gmail.com","abc123",1000,"test address","9876543210","98765432126");
        // WalletRechargeRequest rechargeRequest = new WalletRechargeRequest(100);
        User updatedUser = new User("abc","abc","abc@gmail.com","abc123",1100,"test address","9876543210","98765432126");

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Object> responseEntity = walletService.walletRecharge(anyString(),100);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(transactionRepository,times(0)).save(any());
        verify(userRepository,times(0)).save(any());
    }
    @Test
    public void transferAmountFailedSenderNotPresent()
    {
        TransferRequestDTO transferRequestDTO=new TransferRequestDTO("abc",100);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Object> responseEntity = walletService.walletAmountTransfer(anyString(),transferRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(transactionRepository,times(0)).save(any());
        verify(userRepository,times(0)).save(any());
    }
    @Test
    public void transferAmountFailedReciverNotPresent()
    {
        User sender = new User("abc","abc","abc@gmail.com","abc123",1000,"test address","9876543210","98765432126");
        TransferRequestDTO transferRequestDTO=new TransferRequestDTO("john",100);
        when(userRepository.findById(null)).thenReturn(Optional.ofNullable(sender));
        when(userRepository.findById(transferRequestDTO.getReciver())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Object> responseEntity = walletService.walletAmountTransfer(anyString(),transferRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(transactionRepository,times(0)).save(any());
        verify(userRepository,times(0)).save(any());
    }
    @Test
    public void transferAmountSuccess()
    {
        TransferRequestDTO transferRequestDTO=new TransferRequestDTO("abc",100);
        User sender = new User("abc","abc","abc@gmail.com","abc123",1000,"test address","9876543210","98765432126");
        User updatedSender = new User("abc","abc","abc@gmail.com","abc123",1100,"test address","9876543210","98765432126");
        when(userRepository.findById(any())).thenReturn(Optional.of(sender));
        when(userRepository.save(any())).thenReturn(updatedSender);
        when(transactionRepository.save(any())).thenReturn(new Transaction("","debit","abc","pqr",100,new Date()));
        when(kafkaTemplate.send(anyString(),anyString())).thenReturn(null);
        ResponseEntity<Object> responseEntity = walletService.walletAmountTransfer(anyString(),transferRequestDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      //  verify(transactionRepository,times(1)).save(any());
        verify(userRepository,times(2)).save(any());
    }

    @Test
    public void walletAmountTransfer_InsufficientBalance()
    {
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO("mohan",1000);
        User sender = new User("John","John Deo ","johndeo@gmail.com","john1234",100,"test address","9876543210","98765432126");
        User receiver = new User("mgoyal","Mohit Goyal","mohitgoyal@gmail.com","goyal@123",200.0,"test address","9876543210","98765432126");
        when(userRepository.findById(any())).thenReturn(Optional.of(sender));
        when(userRepository.findById(any())).thenReturn(Optional.of(receiver));
        ResponseEntity<Object> responseEntity = walletService.walletAmountTransfer(anyString(),transferRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        verify(userRepository,times(0)).save(any());
    }

    @Test
    public void showAllTransactionsTest()
    {
        List<Transaction> mockTransactionsList = Stream.of(new
                        Transaction("123456", "wallet-recharge", "bank", "john", 20,new Date()),new
                        Transaction("123457", "tranfer", "jack", "john", 20,new Date()),new
                        Transaction("123458", "transfer", "john", "abc", 20,new Date()),new
                Transaction("123459", "transfer", "john", "Jack", 20,new Date())
        ).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(0, 4, Sort.by("transactionDate").descending());
        Page<Transaction> mockPageTransactions = new PageImpl<>(mockTransactionsList, pageable, mockTransactionsList.size());
        when(transactionRepository.findByReceiverOrSender(any(),any())).thenReturn(mockPageTransactions);
        ResponseEntity<Object> responseEntity = walletService.showAllTransactions(any(),pageable);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof TransactionListDTO);

        TransactionListDTO transactionListDTO = (TransactionListDTO) responseEntity.getBody();
        assertEquals(mockTransactionsList.size(), transactionListDTO.getTransactions().size());
        assertEquals(pageable.getPageNumber(), transactionListDTO.getCurrentPage());
        assertEquals(mockPageTransactions.getTotalElements(), transactionListDTO.getTotalItems());
        assertEquals(mockPageTransactions.getTotalPages(), transactionListDTO.getTotalPages());
    }
    @Test
    public void showBalanceTest()
    {
        User savedUser = new User("john","John Bist","johnbist@gmail.com","johnbist1234",2000,"test address","9876543210","98765432126");
        when(userRepository.findById(any())).thenReturn(Optional.of(savedUser));
        ResponseEntity<Object> responseEntity = walletService.showBalance(any());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(savedUser,responseEntity.getBody());
    }
}
