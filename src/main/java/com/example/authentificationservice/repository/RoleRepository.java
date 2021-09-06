package com.example.authentificationservice.repository;

import com.example.authentificationservice.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface RoleRepository extends MongoRepository<UserRole,String> {

    UserRole findByName(String name);
}
