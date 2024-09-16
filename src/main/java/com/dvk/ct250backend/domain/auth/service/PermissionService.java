package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;

import java.util.Map;

public interface PermissionService {
    Page<PermissionDTO> getAllPermissions(Map<String, String> params);
    PermissionDTO createPermission(PermissionDTO permissionDTO) throws IdInValidException;
    void deletePermission(Long id) throws IdInValidException;
    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) throws IdInValidException;

}
