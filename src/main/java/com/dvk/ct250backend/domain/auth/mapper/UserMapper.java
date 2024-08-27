package com.dvk.ct250backend.domain.auth.mapper;

import com.dvk.ct250backend.domain.country.mapper.CountryMapper;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//@Mapper(componentModel = "spring", uses = {RoleMapper.class, CountryMapper.class})
@Mapper(componentModel = "spring", uses = { CountryMapper.class})
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}

