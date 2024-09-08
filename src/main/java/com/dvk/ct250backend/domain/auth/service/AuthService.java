package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserDTO register(UserDTO userDTO) throws IdInValidException;
    AuthResponse login(AuthRequest authRequest, HttpServletResponse response);
    AuthResponse refreshAccessToken(String refreshToken);
    void logout(HttpServletResponse httpServletResponse) throws IdInValidException;
}
