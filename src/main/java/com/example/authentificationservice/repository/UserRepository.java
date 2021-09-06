package com.example.authentificationservice.repository;

import com.example.authentificationservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByUserName(String name);
}
