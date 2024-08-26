package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
}
