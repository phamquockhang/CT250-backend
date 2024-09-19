package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleDTO createRole (RoleDTO roleDTO) throws ResourceNotFoundException;
    Page<RoleDTO> getRoles(Map<String, String> params);
    RoleDTO getRoleById(Long id) throws ResourceNotFoundException;
    void deleteRole(Long id) throws ResourceNotFoundException;
    RoleDTO updateRole(Long id, RoleDTO roleDTO) throws ResourceNotFoundException;
    List<RoleDTO> getAllRoles();
}
