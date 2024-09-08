package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.PaginationDTO;

import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    RoleDTO createRole (RoleDTO roleDTO) throws IdInValidException;
    PaginationDTO getAllRoles(Specification<Role> spec, Pageable pageable);
    RoleDTO getRoleById(Long id) throws IdInValidException;
    void deleteRole(Long id) throws IdInValidException;
    RoleDTO updateRole(RoleDTO roleDTO) throws IdInValidException;
}
