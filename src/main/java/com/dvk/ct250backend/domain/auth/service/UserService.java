package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO) throws ResourceNotFoundException;
    UserDTO getUserById(UUID id) throws ResourceNotFoundException;
    Page<UserDTO> getUsers(Specification<User> spec, int page, int pageSize, String sort);
    void deleteUser(UUID id) throws ResourceNotFoundException;
    UserDTO updateUser(UUID id, UserDTO userDTO) throws ResourceNotFoundException;
    UserDTO getLoggedInUser();
}