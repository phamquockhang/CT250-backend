package com.dvk.ct250backend.domain.auth.service;

import com.dvk.ct250backend.app.dto.Pagination;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionService {
    Pagination<PermissionDTO> getAllPermissions(Specification<Permission> spec, int page, int pageSize);
    PermissionDTO createPermission(PermissionDTO permissionDTO) throws IdInValidException;
    void deletePermission(Long id) throws IdInValidException;
    PermissionDTO updatePermission(PermissionDTO permissionDTO) throws IdInValidException;

}
