package com.training.WalletPlus.controller;

import com.training.WalletPlus.dto.TransactionListDTO;
import com.training.WalletPlus.dto.TransferRequestDTO;
import com.training.WalletPlus.dto.WalletAmount;
import com.training.WalletPlus.model.Transaction;
import com.training.WalletPlus.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;
    @Mock
    private WalletService walletService;

    @Test
    public void rechargeSucesstest()
    {
        ResponseEntity<Object> entity = new ResponseEntity<>("Recharged Successfully", HttpStatus.OK);
        WalletAmount rechargeRequest = new WalletAmount("abc",500.0);
        String token ="token";
        when(walletService.walletRecharge(anyString(),anyDouble())).thenReturn(entity);

        ResponseEntity<Object> responseEntity = walletController.recharge(token,rechargeRequest);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        verify(walletService,times(1)).walletRecharge(anyString(),anyDouble());
    }
    @Test
    public void walletTransferTest_BadRequest()
    {
        ResponseEntity<Object> entity = new ResponseEntity<>("Receiver Not Found",HttpStatus.BAD_REQUEST);
        TransferRequestDTO amountTransferRequest= new TransferRequestDTO("testuser",2000);
        String token="token";
        when(walletService.walletAmountTransfer(anyString(),any(TransferRequestDTO.class))).thenReturn(entity);
        ResponseEntity<Object> responseEntity = walletController.walletTransfer(token,amountTransferRequest);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }
    @Test
    public void walletTransferTest_Success()
    {
        ResponseEntity<Object> entity = new ResponseEntity<>("Transaction Successfull",HttpStatus.OK);
        TransferRequestDTO amountTransferRequest= new TransferRequestDTO("testuser",2000);
        String token="token";

        when(walletService.walletAmountTransfer(anyString(),any(TransferRequestDTO.class))).thenReturn(entity);
        ResponseEntity<Object> responseEntity = walletController.walletTransfer(token,amountTransferRequest);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void showAllTransactionsTest()
    {
        List<Transaction> alltransactionsList = Stream.of(new
                Transaction("123456", "wallet-recharge", "bank", "john", 20,new Date()),new
                Transaction("123457", "tranfer", "jack", "john", 20,new Date()),new
                Transaction("123458", "transfer", "john", "abc", 20,new Date()),new
                Transaction("123459", "transfer", "john", "Jack", 20,new Date())).collect(Collectors.toList());
        Pageable paging = PageRequest.of(1, 5);

        ResponseEntity<Object> entity = new ResponseEntity<>(new TransactionListDTO(alltransactionsList,0,4,1),HttpStatus.OK);
        when(walletService.showAllTransactions(anyString(),any())).thenReturn(entity);
        ResponseEntity<Object> responseEntity = walletController.showTransactions("mocktoken",0,5);
        TransactionListDTO transactionsListdto = (TransactionListDTO) responseEntity.getBody();
        assertEquals(4,transactionsListdto.getTransactions().size());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
    @Test
    public void showBalanceTest()
    {
        ResponseEntity<Object> entity = new ResponseEntity<>(2000.0,HttpStatus.OK);

        when(walletService.showBalance(anyString())).thenReturn(entity);

        ResponseEntity<Object> responseEntity = walletController.showBalance(anyString());

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(2000.0,
                responseEntity.getBody());
    }
}
