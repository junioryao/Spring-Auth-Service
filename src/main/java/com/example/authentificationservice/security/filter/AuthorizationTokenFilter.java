package com.example.authentificationservice.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.authentificationservice.security.JWT.JWTConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationTokenFilter extends OncePerRequestFilter {
  private final JWTConfig jwtConfig;

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
        try {
          String TOKEN = authorizationHeader.split(" ")[1];
          jwtConfig.verifyAccessToken(TOKEN);
          filterChain.doFilter(request, response);
        } catch (JWTVerificationException exception) {
          // Invalid signature/claims
          log.error("authorization failed " + exception.getMessage());
          response.setHeader("error", exception.getMessage());
          response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}
