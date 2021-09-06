package com.example.authentificationservice.api;

import com.example.authentificationservice.service.UserServiceImplementation;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class UserControlerTest {

  @MockBean UserServiceImplementation userServiceImplementation;
  @Autowired MockMvc mockMvc;
  @Autowired PasswordEncoder passwordEncoder;
  private final Gson gson = new Gson();

  @Test
  @DisplayName("is not Unauthorized => Authentication failed or not define ")
  void getAUserDetails() {}

  @Test
  void getAllUserDetails() {}

  @Test
  void putRoleToUser() {}

  @Test
  void insertANewRole() {}
}
