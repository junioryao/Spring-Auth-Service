package com.example.authentificationservice.security;

import com.example.authentificationservice.security.JWT.JWTProcess;
import com.example.authentificationservice.service.UserServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class securitySettingConfig {

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  JWTProcess jwtProcess() {
    return new JWTProcess();
  }

}
