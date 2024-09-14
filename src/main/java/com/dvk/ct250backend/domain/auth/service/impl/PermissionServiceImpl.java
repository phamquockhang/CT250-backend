package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.Meta;
import com.dvk.ct250backend.app.dto.Page;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.mapper.PermissionMapper;
import com.dvk.ct250backend.domain.auth.repository.PermissionRepository;
import com.dvk.ct250backend.domain.auth.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    public Page<PermissionDTO> getAllPermissions(Specification<Permission> spec, int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        org.springframework.data.domain.Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(permissionPage.getTotalPages())
                .total(permissionPage.getTotalElements())
                .build();
        return Page.<PermissionDTO>builder()
                .meta(meta)
                .content(permissionPage.getContent().stream()
                        .map(permissionMapper::toPermissionDTO)
                        .toList())
                .build();
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