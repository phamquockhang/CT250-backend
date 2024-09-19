package com.dvk.ct250backend.domain.auth.mapper;

import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);
    Role toRole(RoleDTO roleDTO);

    void updateRoleFromDTO(@MappingTarget Role role, RoleDTO roleDTO);
}
