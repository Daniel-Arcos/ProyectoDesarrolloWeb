package com.web.aldalu.aldalu.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.web.aldalu.aldalu.utils.AuthConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler{

    @Override
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication
  ) {
    final String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
    final String jwt;
    if (authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
      return;
    }
    jwt = authHeader.substring(AuthConstants.BEARER_PREFIX.length());
  }
}
