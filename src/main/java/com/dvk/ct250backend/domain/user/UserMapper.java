package com.dvk.ct250backend.domain.user;

import com.dvk.ct250backend.domain.CountryMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, CountryMapper.class})
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
