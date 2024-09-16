package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
            @Filter Specification<User> spec,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sort) {
        return ApiResponse.<Page<UserDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.getUsers(spec, page, pageSize, sort))
                .build();
    }

    @GetMapping("/logged-in")
    public ApiResponse<UserDTO> getLoggedInUser() {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.getLoggedInUser())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.getUserById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping
    public ApiResponse<UserDTO> updateUser(@RequestBody UserDTO user) throws ResourceNotFoundException {
        return ApiResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(userService.updateUser(user))
                .build();
    }


}