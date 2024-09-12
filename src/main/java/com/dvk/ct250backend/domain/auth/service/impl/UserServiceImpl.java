package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.dto.Meta;
import com.dvk.ct250backend.app.dto.Page;
import com.dvk.ct250backend.app.exception.IdInValidException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.mapper.UserMapper;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.auth.service.UserService;
import com.dvk.ct250backend.domain.country.entity.Country;
import com.dvk.ct250backend.domain.country.service.CountryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CountryService countryService;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) throws IdInValidException {
        validateUserDetails(userDTO);
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userMapper.toUserDTO(userRepository.save(user));
    }

    private void validateUserDetails(UserDTO userDTO) throws IdInValidException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IdInValidException("Email " + userDTO.getEmail() + " already exists, please use a different email.");
        }

        Map<String, String> requiredFields = Map.of(
            "First name", userDTO.getFirstName(),
            "Last name", userDTO.getLastName(),
            "Gender", userDTO.getGender(),
            "Identity number", userDTO.getIdentityNumber(),
            "Phone number", userDTO.getPhoneNumber()
        );

        requiredFields.forEach((field, value) -> {
            if (value == null || value.isEmpty()) {
                try {
                    throw new IdInValidException(field + " must be provided.");
                } catch (IdInValidException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if (userDTO.getDateOfBirth() == null) {
            throw new IdInValidException("Date of birth must be provided.");
        }
        if (userDTO.getCountryId() == null) {
            throw new IdInValidException("Country ID must be provided.");
        }
//        if (userDTO.getRoleId() == null) {
//            throw new IdInValidException("Role ID must be provided.");
//        }
    }

    @Override
    public Page<UserDTO> getAllUsers(Specification<User> spec, int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
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
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " is invalid."));
        return userMapper.toUserDTO(user);
    }

    @Override
    public void deleteUser(UUID id) throws IdInValidException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdInValidException("User ID " + id + " is invalid."));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO) throws IdInValidException {
        User user = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new IdInValidException("User ID " + userDTO.getUserId() + " is invalid."));
        userMapper.updateUserFromDTO(userDTO, user);
        if (userDTO.getCountryId() != null) {
            Country country = countryService.findById(userDTO.getCountryId())
                    .orElseThrow(() -> new IdInValidException("Country ID " + userDTO.getCountryId() + " is invalid.") );
            user.setCountry(country);
        }
        return userMapper.toUserDTO(userRepository.save(user));
    }
}