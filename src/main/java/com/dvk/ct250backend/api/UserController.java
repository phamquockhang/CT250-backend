package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.ChangePasswordRequest;
import com.dvk.ct250backend.domain.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(userService.createUser(userDTO))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<UserDTO>> getAllUser(
            @RequestParam Map<String, String> params) {
        return ApiResponse.<Page<UserDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.getUsers(params))
                .build();
    }

    @GetMapping("/logged-in")
    public ApiResponse<UserDTO> getLoggedInUser() {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.getLoggedInUser())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping("{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable UUID id,@RequestBody UserDTO user) throws ResourceNotFoundException {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.updateUser(id, user))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.getUserById(id))
                .build();
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException {
        userService.changePassword(id, changePasswordRequest);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

}