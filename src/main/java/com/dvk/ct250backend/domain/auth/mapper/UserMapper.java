package com.dvk.ct250backend.domain.auth.mapper;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.country.entity.Country;
import com.dvk.ct250backend.domain.country.mapper.CountryMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {RoleMapper.class, CountryMapper.class})
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role.permissions", ignore = true)
    void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user);

}

