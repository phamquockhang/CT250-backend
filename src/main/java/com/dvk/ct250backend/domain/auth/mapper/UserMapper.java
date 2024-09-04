package com.dvk.ct250backend.domain.auth.mapper;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleName")
    @Mapping(target = "countryId", source = "country.countryId")
    @Mapping(target = "userId", source = "userId")
    UserDTO toUserDTO(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "country.countryId", source = "countryId")
    @Mapping(target = "userId", source = "userId")
    User toUser(UserDTO userDTO);

//    @Named("rolesToRoleName")
//    default Set<String> rolesToRoleName(Set<Role> roles){
//        return roles.stream().map(Role::getRoleName).collect(Collectors.toSet());
//    }
    @Named("rolesToRoleName")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleName")
    default Set<String> rolesToRoleName(Set<Role> roles) {
        return roles != null ? roles.stream().map(Role::getRoleName).collect(Collectors.toSet()) : new HashSet<>();
    }

}