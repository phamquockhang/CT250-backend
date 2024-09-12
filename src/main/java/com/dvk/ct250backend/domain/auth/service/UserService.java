package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.Pagination;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO) throws IdInValidException;
    UserDTO getUserById(UUID id) throws IdInValidException;
    Pagination<UserDTO> getAllUsers(Specification<User> spec, int page, int pageSize);
    void deleteUser(UUID id) throws IdInValidException;
    UserDTO updateUser(UserDTO userDTO) throws IdInValidException;
}