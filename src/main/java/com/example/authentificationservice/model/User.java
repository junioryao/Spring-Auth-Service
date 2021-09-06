package com.example.authentificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
  @Id private String Id;

  private String userName;

  private String userLastName;

  private String password;

  private String email;

  private List<UserRole> Roles;
}
