package com.example.authentificationservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationsFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    log.info(" user is trying to log in {} => {} ", username, password);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);
    return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {

    // what to do when the user is authenticated => generate is JWT

    // select the user which came from user details service
    User loggingUser = (User) authResult.getPrincipal();

    // generate the Json Token
    Algorithm algorithm = Algorithm.HMAC256("junioryao".getBytes());
    String accessToken =
        JWT.create()
            .withSubject(loggingUser.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000)))
            .withArrayClaim(
                "role",
                loggingUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList())
                    .toArray(String[]::new))
            .sign(algorithm);

    // return to the user the JWT => by setting an header

    response.setHeader("accessToken", accessToken);
    log.info("user is log successfully");
    // super.successfulAuthentication(request, response, chain, authResult);
  }
}
