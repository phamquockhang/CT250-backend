package com.dvk.ct250backend.domain.user.mapper;

import com.dvk.ct250backend.domain.user.dto.RoleDTO;
import com.dvk.ct250backend.domain.user.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);
    Role toRole(RoleDTO roleDTO);
}
