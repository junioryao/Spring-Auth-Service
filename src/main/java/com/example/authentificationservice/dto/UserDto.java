package com.example.authentificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.authentificationservice.model.UserRole;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
  private String userName;

  private String userLastName;

  private String password;

  private String email;

  private List<UserRole> Roles = new ArrayList<>();
}
