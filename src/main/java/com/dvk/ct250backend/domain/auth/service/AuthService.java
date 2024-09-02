package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserDTO register(UserDTO userDTO);
    AuthResponse login(AuthRequest authRequest, HttpServletResponse response);
    AuthResponse refreshAccessToken(String refreshToken);
}
