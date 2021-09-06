package com.example.authentificationservice.security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTConfig {

  private final String tokenSecret = "juniorYao";

  private final Algorithm algorithm = Algorithm.HMAC256(tokenSecret.getBytes());

  private final JWTVerifier verifier = JWT.require(algorithm).build();

  public String getAccessToken(User loggingUser) {

    return JWT.create()
        .withSubject(loggingUser.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000)))
        .withArrayClaim(
            "role",
            loggingUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .toArray(String[]::new))
        .sign(this.algorithm);
  }

  public void verifyAccessToken(String token) {
    // verify that token
    DecodedJWT jwt = verifier.verify(token);
    // put every authority in a class object of simpleGranted Authority
    log.info(jwt.getSubject());
    log.info(String.valueOf(jwt.getClaim("role")));

    var roleList = jwt.getClaim("role").asList(String.class);
    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
    roleList.forEach(
        s -> {
          authorityList.add(new SimpleGrantedAuthority(s));
        });
    // pass the user info to the application
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, authorityList);
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }
}
