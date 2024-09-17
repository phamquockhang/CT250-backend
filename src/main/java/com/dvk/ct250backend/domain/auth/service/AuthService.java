package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface AuthService {
    UserDTO register(UserDTO userDTO) throws ResourceNotFoundException;
    AuthResponse login(AuthRequest authRequest, HttpServletResponse response);
    AuthResponse refreshAccessToken(String refreshToken);
    void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException;
    UserDTO processOAuthPostLogin(OidcUser oidcUser);
}
