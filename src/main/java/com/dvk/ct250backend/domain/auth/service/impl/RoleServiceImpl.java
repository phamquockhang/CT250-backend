//package com.dvk.ct250backend.domain.auth.service.impl;
//
//import com.dvk.ct250backend.app.exception.IdInValidException;
//import com.dvk.ct250backend.domain.auth.dto.RoleDTO;
//import com.dvk.ct250backend.domain.auth.entity.Role;
//import com.dvk.ct250backend.domain.auth.mapper.RoleMapper;
//import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
//import com.dvk.ct250backend.domain.auth.service.RoleService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class RoleServiceImpl implements RoleService {
//    RoleRepository roleRepository;
//    RoleMapper roleMapper;
//
//    @Override
//    public RoleDTO createRole(RoleDTO roleDTO) throws IdInValidException {
//        if(this.roleRepository.existsByRoleName(roleDTO.getRoleName())) {
//            throw new IdInValidException("Role with name = " + roleDTO.getRoleName() + " already exists");
//        }
//        Role role = roleRepository.save(roleMapper.toRole(roleDTO));
//        return roleMapper.toRoleDTO(role);
//    }
//
//    @Override
//    public RoleDTO getAllRoles() {
//        return roleMapper.toRoleDTO((Role) roleRepository.findAll());
//    }
//}
