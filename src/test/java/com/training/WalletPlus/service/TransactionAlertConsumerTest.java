package com.training.WalletPlus.service;

import com.training.WalletPlus.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.verify;

@EnableKafka
@TestPropertySource(properties = {
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionAlertConsumerTest {

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private EmailService emailService; // Mocked in this example
//
//    @Test
//    public void testTransactionAlertListener() {
//        // Simulate a message being sent to the Kafka topic
//        String message = "Sample transaction alert message";
//        kafkaTemplate.send("transaction-alerts", message);
//
//        // Add any necessary assertions here based on your specific use case
//
//        // Verify that the emailService's sendTransactionAlertEmail method was called with the expected message
//        verify(emailService).sendTransactionAlertEmail(message);
//    }
}
