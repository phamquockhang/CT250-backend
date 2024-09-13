package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.dto.response.AuthResponse;
import com.dvk.ct250backend.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody UserDTO userDTO) throws IdInValidException {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(authService.register(userDTO))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest,
                                           HttpServletResponse response) {
        return ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .payload(authService.login(authRequest, response))
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshAccessToken(@CookieValue("refresh_token") String refreshToken) {
        return ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .payload(authService.refreshAccessToken(refreshToken))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) throws IdInValidException {
        authService.logout(response);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }
}