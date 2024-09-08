package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.PaginationDTO;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.PermissionDTO;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.entity.User;
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

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PaginationDTO getAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = permissionRepository.findAll(spec, pageable);
        PaginationDTO.Meta meta = new PaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pagePermission.getTotalPages());
        meta.setTotal(pagePermission.getTotalElements());

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setMeta(meta);

        paginationDTO.setResult(pagePermission.getContent());

        return paginationDTO;
    }

    @Override
    @Transactional
    public PermissionDTO createPermission(PermissionDTO permissionDTO) throws IdInValidException {
       if(permissionRepository.existsByModuleAndApiPathAndMethod(permissionDTO.getModule(), permissionDTO.getApiPath(), permissionDTO.getMethod())) {
           throw new IdInValidException("Permission already exists");
       }
       Permission permission = permissionMapper.toPermission(permissionDTO);

        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionDTO(permission);
    }

    @Override
    public void deletePermission(Long id) throws IdInValidException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IdInValidException("Permission ID " + id + " is invalid."));
        permissionRepository.delete(permission);
    }

    @Override
    public PermissionDTO updatePermission(PermissionDTO permissionDTO) throws IdInValidException {
        Permission permission = permissionMapper.toPermission(permissionDTO);
        Permission permissionDB = permissionRepository.findById(permission.getPermissionId())
                .orElseThrow(() -> new IdInValidException("Permission ID " + permission.getPermissionId() + " is invalid."));

        if (!permissionDB.getName().equals(permissionDTO.getName())) {
            permissionDB.setName(permissionDTO.getName());
        }
        permissionDB = permissionRepository.save(permissionDB);
        return permissionMapper.toPermissionDTO(permissionDB);
    }

}