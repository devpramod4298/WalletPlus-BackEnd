package com.training.WalletPlus.repo;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.WalletPlus.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String>{
  List<Transaction> findAllBySender(String senderUserName);
  List<Transaction> findAllByReceiver(String receiverUserName);
  @Query("{$or:[{'receiver':?0}, {'sender':?0}]}")
  Page<Transaction> findByReceiverOrSender(@Param("name") String name, Pageable pageable);
}
