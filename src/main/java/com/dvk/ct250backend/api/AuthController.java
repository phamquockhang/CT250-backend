package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.dto.response.AuthResponse;
import com.dvk.ct250backend.domain.auth.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody UserDTO userDTO, @RequestHeader("siteUrl") String siteUrl) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(authService.register(userDTO, siteUrl))
                .build();
    }

    @GetMapping("/verify")
    public ApiResponse<UserDTO> verifyUser(@RequestParam("token") String token) throws ResourceNotFoundException {
        UserDTO userDTO = authService.verifyUser(token);

        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userDTO)
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
    public ApiResponse<Void> logout(HttpServletResponse response) throws ResourceNotFoundException {
        authService.logout(response);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/success")
    public ApiResponse<UserDTO> loginSuccess(@AuthenticationPrincipal OidcUser oidcUser) {
        UserDTO userDTO = authService.processOAuthPostLogin(oidcUser);
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userDTO)
                .build();
    }



    @GetMapping("/failure")
    public ApiResponse<String> loginFailure() {
        return ApiResponse.<String>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .payload("Login failed")
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestParam String email, @RequestParam String siteUrl) {
        try {
            authService.forgotPassword(email, siteUrl);
            return ApiResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .payload("Password reset link has been sent to your email.")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .payload(e.getMessage())
                    .build();
        }
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            authService.resetPassword(token, newPassword);
            return ApiResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .payload("Password has been reset successfully.")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .payload(e.getMessage())
                    .build();
        }
    }


    @GetMapping("/verify-reset-token")
    public ApiResponse<Void> verifyResetToken(@RequestParam String token) throws ResourceNotFoundException {
        authService.verifyResetToken(token);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

}