package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.PaginationDTO;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.mapper.PermissionMapper;
import com.dvk.ct250backend.domain.auth.repository.PermissionRepository;
import com.dvk.ct250backend.domain.auth.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PaginationDTO getAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = permissionRepository.findAll(spec, pageable);
        PaginationDTO.Meta meta = createMeta(pageable, pagePermission);

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setMeta(meta);
        paginationDTO.setResult(pagePermission.getContent());

        return paginationDTO;
    }

    @Override
    @Transactional
    public PermissionDTO createPermission(PermissionDTO permissionDTO) throws IdInValidException {
        if (permissionRepository.existsByModuleAndApiPathAndMethod(permissionDTO.getModule(), permissionDTO.getApiPath(), permissionDTO.getMethod())) {
            throw new IdInValidException("Permission already exists");
        }
        Permission permission = permissionMapper.toPermission(permissionDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionDTO(permission);
    }

    @Override
    public void deletePermission(Long id) throws IdInValidException {
        Permission permission = findPermissionById(id);
        permissionRepository.delete(permission);
    }

    @Override
    public PermissionDTO updatePermission(PermissionDTO permissionDTO) throws IdInValidException {
        Permission permission = permissionMapper.toPermission(permissionDTO);
        Permission permissionDB = findPermissionById(permission.getPermissionId());

        updatePermissionDetails(permissionDB, permissionDTO);
        permissionDB = permissionRepository.save(permissionDB);
        return permissionMapper.toPermissionDTO(permissionDB);
    }

    private PaginationDTO.Meta createMeta(Pageable pageable, Page<Permission> pagePermission) {
        PaginationDTO.Meta meta = new PaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pagePermission.getTotalPages());
        meta.setTotal(pagePermission.getTotalElements());
        return meta;
    }

    private Permission findPermissionById(Long id) throws IdInValidException {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new IdInValidException("Permission ID " + id + " is invalid."));
    }

    private void updatePermissionDetails(Permission permissionDB, PermissionDTO permissionDTO) {
        if (!permissionDB.getName().equals(permissionDTO.getName())) {
            permissionDB.setName(permissionDTO.getName());
        }
    }
}