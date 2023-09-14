package com.training.WalletPlus.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class TransactionTest {

    @Test
    public void testConstructorAndGetters() {
        String transactionId = "12345";
        String transactionType = "debit";
        String sender = "User1";
        String receiver = "User2";
        double amount = 100.0;
        Date transactionDate = new Date();

        Transaction transaction = new Transaction(transactionId, transactionType, sender, receiver, amount, transactionDate);

        assertEquals(transactionId, transaction.getTransactionId());
        assertEquals(transactionType, transaction.getTransactionType());
        assertEquals(sender, transaction.getSender());
        assertEquals(receiver, transaction.getReceiver());
        assertEquals(amount, transaction.getAmount());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    public void testEqualsAndHashCode() {
        Transaction transaction1 = new Transaction("12345", "debit", "User1", "User2", 100.0, new Date());
        Transaction transaction2 = new Transaction("12345", "debit", "User1", "User2", 100.0, new Date());

        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    public void testToString() {
        Transaction transaction = new Transaction("12345", "debit", "User1", "User2", 100.0, new Date());

        String expectedToString = "Transaction(transactionId=12345, transactionType=debit, sender=User1, receiver=User2, amount=100.0, transactionDate=" + transaction.getTransactionDate() + ")";
        assertEquals(expectedToString, transaction.toString());
    }
}
