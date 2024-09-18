package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;

import java.util.List;
import java.util.Map;

public interface PermissionService {
    Page<PermissionDTO> getPermissions(Map<String, String> params);
    PermissionDTO createPermission(PermissionDTO permissionDTO) throws ResourceNotFoundException;
    void deletePermission(Long id) throws ResourceNotFoundException;
    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) throws ResourceNotFoundException;
    List<PermissionDTO> getAllPermissions();
}
