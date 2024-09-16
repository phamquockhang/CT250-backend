package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    RoleDTO createRole (RoleDTO roleDTO) throws ResourceNotFoundException;
    Page<RoleDTO> getAllRoles(Specification<Role> spec, int page, int pageSize, String sort);
    RoleDTO getRoleById(Long id) throws ResourceNotFoundException;
    void deleteRole(Long id) throws ResourceNotFoundException;
    RoleDTO updateRole(RoleDTO roleDTO) throws ResourceNotFoundException;
}
