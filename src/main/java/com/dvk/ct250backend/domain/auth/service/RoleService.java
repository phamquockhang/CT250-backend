package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.Pagination;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    RoleDTO createRole (RoleDTO roleDTO) throws IdInValidException;
    Pagination<RoleDTO> getAllRoles(Specification<Role> spec, int page, int pageSize);
    RoleDTO getRoleById(Long id) throws IdInValidException;
    void deleteRole(Long id) throws IdInValidException;
    RoleDTO updateRole(RoleDTO roleDTO) throws IdInValidException;
}
