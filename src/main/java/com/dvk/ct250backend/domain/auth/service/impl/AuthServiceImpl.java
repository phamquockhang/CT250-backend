package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.mapper.UserMapper;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.auth.service.AuthService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO login(AuthRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getPassword().equals(loginRequest.getPassword())) {
            return userMapper.toUserDTO(user);
        } else {
            throw new RuntimeException("Invalid password");
        }
    }
}