package com.example.authentificationservice.api;

import com.example.authentificationservice.service.UserServiceImplementation;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControlerTest {

  @MockBean UserServiceImplementation userServiceImplementation;
  @Autowired MockMvc mockMvc;
  @Autowired PasswordEncoder passwordEncoder;
  private final Gson gson = new Gson();

  @Test
  @DisplayName("is not Unauthorized => Authentication failed or not define ")
  void getAUserDetails() throws Exception {
    when(userServiceImplementation.getUser("junior")).thenReturn(DAOutils.getUserDTO());
    mockMvc
        .perform(get("/v2/user/").contentType(MediaType.APPLICATION_JSON).param("name", "junior"))
        .andExpect(status().isUnauthorized());
    verify(userServiceImplementation, times(0)).getUser("junior");
  }
  /*

    @Test
    void getAUserDetails() throws Exception {
      System.out.println(DAOutils.getUserDTO());

      when(userServiceImplementation.getUser("junior")).thenReturn(DAOutils.getUserDTO());
      mockMvc
          .perform(get("/v2/user/").contentType(MediaType.APPLICATION_JSON).param("name", "junior"))
          .andExpect(status().isUnauthorized())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(content().json(gson.toJson(DAOutils.getUserDTO())));

      verify(userServiceImplementation.getUser("junior"), times(1));
    }
  */

  @Test
  void getAllUserDetails() {}

  @Test
  void putRoleToUser() {}

  @Test
  void insertANewRole() {}
}
