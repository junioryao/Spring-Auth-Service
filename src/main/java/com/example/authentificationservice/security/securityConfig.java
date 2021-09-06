package com.example.authentificationservice.security;

import com.example.authentificationservice.security.JWT.JWTProcess;
import com.example.authentificationservice.security.filter.AuthenticationsFilter;
import com.example.authentificationservice.security.filter.AuthorizationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.privilege.GetSystemPropertyAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class securityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;
  private final JWTProcess jwtProcess;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // track jwt token

    http.csrf().disable();
    http.authorizeRequests().mvcMatchers("/login/**",  "/v2/user/refreshToken/**").permitAll();
    http.authorizeRequests().anyRequest().authenticated();
    // add our auth filter
    http.addFilter(new AuthenticationsFilter(authenticationManagerBean(), jwtProcess));
    // authorization filter => verify the JWT token provided
    http.addFilterBefore(
        new AuthorizationTokenFilter(jwtProcess), UsernamePasswordAuthenticationFilter.class);
    // login filter => provide JWT token during connection

  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
