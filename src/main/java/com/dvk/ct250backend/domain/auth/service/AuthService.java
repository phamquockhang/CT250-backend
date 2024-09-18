package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.dto.response.AuthResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    UserDTO register(UserDTO userDTO, String  siteUrl) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException;
    AuthResponse login(AuthRequest authRequest, HttpServletResponse response);
    AuthResponse refreshAccessToken(String refreshToken);
    void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException;
    UserDTO processOAuthPostLogin(OidcUser oidcUser);
    UserDTO verifyUser(String token) throws ResourceNotFoundException;
}
