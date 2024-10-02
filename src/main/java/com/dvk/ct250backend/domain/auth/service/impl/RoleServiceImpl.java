package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.mapper.RoleMapper;
import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
import com.dvk.ct250backend.domain.auth.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public Page<RoleDTO> getRoles(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        org.springframework.data.domain.Page<Role> pageRole = roleRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageRole.getNumber() + 1)
                .pageSize(pageRole.getSize())
                .pages(pageRole.getTotalPages())
                .total(pageRole.getTotalElements())
                .build();

        return Page.<RoleDTO>builder()
                .meta(meta)
                .content(pageRole.getContent().stream()
                        .map(roleMapper::toRoleDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleDTO).collect(Collectors.toList());

    }

    @Override
    public RoleDTO getRoleById(Long id) throws ResourceNotFoundException {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
    }

    @Override
    public void deleteRole(Long id) throws ResourceNotFoundException {
        if (!roleRepository.existsRoleByRoleId(id)) {
            throw new ResourceNotFoundException("Role ID " + id + " is invalid.");
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) throws ResourceNotFoundException {
        Role roleToUpdate = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
        roleMapper.updateRoleFromDTO(roleToUpdate, roleDTO);
        return roleMapper.toRoleDTO(roleRepository.save(roleToUpdate));
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) throws ResourceNotFoundException {
        Role newRole = roleRepository.save(roleMapper.toRole(roleDTO));
        return roleMapper.toRoleDTO(newRole);
    }

}