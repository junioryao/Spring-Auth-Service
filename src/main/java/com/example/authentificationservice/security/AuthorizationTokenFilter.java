package com.example.authentificationservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class AuthorizationTokenFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // if it is login let it pass
    log.info("filter authorization ");
    if (request.getServletPath().equals("/login")) {
      // do nothing => pass to the next filter
      filterChain.doFilter(request, response);
    } else {
      String authorizationHeader = request.getHeader(AUTHORIZATION);
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
        String Token = authorizationHeader.split(" ")[1];
        // verify that token
        try {
          Algorithm algorithm = Algorithm.HMAC256("junioryao".getBytes());
          JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
          DecodedJWT jwt = verifier.verify(Token);
          log.info(jwt.getSubject());
          log.info(String.valueOf(jwt.getClaim("role")));

          // put every authority in a class object of simpleGranted Authority
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

          filterChain.doFilter(request,response);
        } catch (JWTVerificationException exception) {
          // Invalid signature/claims
          log.error("authorization failed");
          log.error(exception.getMessage());
          response.setHeader("error",exception.getMessage());
          response.sendError(HttpServletResponse.SC_FORBIDDEN);

        }
      }else {
        filterChain.doFilter(request,response);
      }
    }
  }
}
