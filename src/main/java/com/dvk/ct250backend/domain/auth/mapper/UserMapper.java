package com.dvk.ct250backend.domain.auth.mapper;

import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.enums.GenderEnum;
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

    @BeforeMapping
    default void handleCustomMappings(UserDTO userDTO, @MappingTarget User user) {
        if (userDTO.getCountry() != null) {
            user.setCountry(Country.builder().countryId(userDTO.getCountry().getCountryId()).build());
        }
        if (userDTO.getRole() != null) {
            Role role = user.getRole();
            if (role == null || !role.getRoleId().equals(userDTO.getRole().getRoleId())) {
                user.setRole(Role.builder().roleId(userDTO.getRole().getRoleId()).build());
            }
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAvatar(userDTO.getAvatar());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setIdentityNumber(userDTO.getIdentityNumber());
        user.setGender(GenderEnum.valueOf(userDTO.getGender()));
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setActive(userDTO.isActive());
    }
}

