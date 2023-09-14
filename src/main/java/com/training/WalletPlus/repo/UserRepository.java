package com.training.WalletPlus.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.training.WalletPlus.model.User;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Boolean existsByUserName(String username);
    Boolean existsByEmailId(String email);
    Optional<User> findById(String username);
    Optional<User>findByEmailId(String email);
}