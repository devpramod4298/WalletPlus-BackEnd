package com.training.WalletPlus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionAlertConsumer {
    @Autowired
   EmailService emailService;
    @KafkaListener(topics = "transaction-alerts", groupId = "transaction-alert-consumer")
    public void listen(String message) {
        // Process the message and send emails using the EmailService
        emailService.sendTransactionAlertEmail(message);
    }
}
