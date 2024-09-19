package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.mapper.PermissionMapper;
import com.dvk.ct250backend.domain.auth.mapper.UserMapper;
import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.auth.service.UserService;
import com.dvk.ct250backend.domain.country.service.CountryService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RequestParamUtils requestParamUtils;
    RoleRepository roleRepository;
    PermissionMapper permissionMapper;


    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) throws ResourceNotFoundException {
//        validateUserDetails(userDTO);
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userMapper.toUserDTO(userRepository.save(user));
    }

//    private void validateUserDetails(UserDTO userDTO) throws ResourceNotFoundException {
//        if (userRepository.existsByEmail(userDTO.getEmail())) {
//            throw new ResourceNotFoundException("Email " + userDTO.getEmail() + " already exists, please use a different email.");
//        }
//
//        Map<String, String> requiredFields = Map.of(
//            "First name", userDTO.getFirstName(),
//            "Last name", userDTO.getLastName(),
//            "Gender", userDTO.getGender(),
//            "Identity number", userDTO.getIdentityNumber(),
//            "Phone number", userDTO.getPhoneNumber()
//        );
//
//        requiredFields.forEach((field, value) -> {
//            if (value == null || value.isEmpty()) {
//                try {
//                    throw new ResourceNotFoundException(field + " must be provided.");
//                } catch (ResourceNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//
//        if (userDTO.getDateOfBirth() == null) {
//            throw new ResourceNotFoundException("Date of birth must be provided.");
//        }
//        if (userDTO.getCountryId() == null) {
//            throw new ResourceNotFoundException("Country ID must be provided.");
//        }
//        if (userDTO.getRoleId() == null) {
//            throw new IdInValidException("Role ID must be provided.");
//        }
//    }

    @Override
    public Page<UserDTO> getUsers(Specification<User> spec, int page, int pageSize, String sort) {
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(sort);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<User> userPage = userRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(userPage.getTotalPages())
                .total(userPage.getTotalElements())
                .build();

        return Page.<UserDTO>builder()
                .meta(meta)
                .content(userPage.getContent().stream()
                        .map(userMapper::toUserDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserDTO getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + authentication.getName()));
            return userMapper.toUserDTO(user);
        }
        return null;
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " is invalid."));
        return userMapper.toUserDTO(user);
    }

    @Override
    public void deleteUser(UUID id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User ID " + id + " is invalid."));
        userRepository.delete(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User ID " + id + " is invalid."));
        userMapper.updateUserFromDTO(user, userDTO);
        return userMapper.toUserDTO(userRepository.save(user));
    }
}