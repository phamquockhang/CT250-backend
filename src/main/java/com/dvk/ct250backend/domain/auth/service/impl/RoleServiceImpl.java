package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.Meta;
import com.dvk.ct250backend.app.dto.Pagination;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.mapper.RoleMapper;
import com.dvk.ct250backend.domain.auth.repository.PermissionRepository;
import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
import com.dvk.ct250backend.domain.auth.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public Pagination<RoleDTO> getAllRoles(Specification<Role> spec, int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        Page<Role> pageRole = roleRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageRole.getNumber() + 1)
                .pageSize(pageRole.getSize())
                .pages(pageRole.getTotalPages())
                .total(pageRole.getTotalElements())
                .build();

        return Pagination.<RoleDTO>builder()
                .meta(meta)
                .result(pageRole.getContent().stream().map(roleMapper::toRoleDTO).collect(Collectors.toList()))
                .build();
    }

    @Override
    public RoleDTO getRoleById(Long id) throws IdInValidException {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleDTO)
                .orElseThrow(() -> new IdInValidException("Role ID " + id + " is invalid."));
    }

    @Override
    public void deleteRole(Long id) throws IdInValidException {
        if (!roleRepository.existsRoleByRoleId(id)) {
            throw new IdInValidException("Role ID " + id + " is invalid.");
        }
        roleRepository.deleteById(id);
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO) throws IdInValidException {
        if (!roleRepository.existsRoleByRoleId(Long.valueOf(roleDTO.getRoleId()))) {
            throw new IdInValidException("Role ID " + roleDTO.getRoleId() + " is invalid.");
        }

//        setPermissions(roleDTO);

        Role role = roleMapper.toRole(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toRoleDTO(role);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) throws IdInValidException {
//        setPermissions(roleDTO);

        Role role = roleMapper.toRole(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toRoleDTO(role);
    }

//    private void setPermissions(RoleDTO roleDTO) {
//        if (roleDTO.getPermissions() != null) {
//            List<Long> reqPermissions = roleDTO.getPermissions()
//                    .stream().map(Permission::getPermissionId)
//                    .collect(Collectors.toList());
//            List<Permission> dbPermissions = permissionRepository.findByPermissionIdIn(reqPermissions);
//            roleDTO.setPermissions(dbPermissions);
//        }
//    }
}