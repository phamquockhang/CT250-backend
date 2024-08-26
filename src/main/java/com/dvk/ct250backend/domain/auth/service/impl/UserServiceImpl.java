package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.mapper.UserMapper;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    UserMapper userMapper;
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }
}