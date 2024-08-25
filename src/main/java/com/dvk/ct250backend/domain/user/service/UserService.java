package com.dvk.ct250backend.domain.user.service;

import com.dvk.ct250backend.domain.user.dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
}
