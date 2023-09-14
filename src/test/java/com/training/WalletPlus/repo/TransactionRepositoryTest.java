package com.training.WalletPlus.repo;

import com.training.WalletPlus.repo.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.training.WalletPlus.model.Transaction;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testFindAllBySender() {
        // Create test data
        Transaction transaction1 = new Transaction("1", "debit", "User1", "User2", 100.0, new Date());
        Transaction transaction2 = new Transaction("2", "debit", "User1", "User3", 150.0, new Date());
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        // Perform the repository call
        List<Transaction> foundTransactions = transactionRepository.findAllBySender("User1");

        // Verify the results
        assertEquals(2, foundTransactions.size());
    }
    @Test
    public void testFindAllByReceiver() {
        // Create test data
        Transaction transaction1 = new Transaction("1", "debit", "User1", "User2", 100.0, new Date());
        Transaction transaction2 = new Transaction("2", "debit", "User3", "User1", 150.0, new Date());
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        // Perform the repository call
        List<Transaction> foundTransactions = transactionRepository.findAllByReceiver("User1");

        // Verify the results
        assertEquals(1, foundTransactions.size());
    }
}

