package com.dvk.ct250backend.domain.auth.service.impl;

import com.dvk.ct250backend.app.exception.DuplicateResourceException;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.auth.dto.UserDTO;
import com.dvk.ct250backend.domain.auth.dto.request.AuthRequest;
import com.dvk.ct250backend.domain.auth.dto.response.AuthResponse;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.mapper.UserMapper;
import com.dvk.ct250backend.domain.auth.repository.PermissionRepository;
import com.dvk.ct250backend.domain.auth.repository.RoleRepository;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.domain.auth.service.AuthService;
import com.dvk.ct250backend.domain.auth.service.EmailService;
import com.dvk.ct250backend.infrastructure.audit.AuditAwareImpl;
import com.dvk.ct250backend.infrastructure.utils.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    JwtDecoder jwtDecoder;
    AuditAwareImpl auditAware;
    PermissionRepository permissionRepository;
    EmailService emailService;


    @Override
    @Transactional
    public UserDTO register(UserDTO userDTO, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        boolean isEmailExist = this.userRepository.existsByEmail(userDTO.getEmail());
        if (isEmailExist) {
           // throw new ResourceNotFoundException("Email " + userDTO.getEmail() + " already exists, please use another email.");
            throw new DuplicateResourceException("Email " + userDTO.getEmail() + " already exists, please use another email.");
        }
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        user.setVerificationToken(UUID.randomUUID().toString());

        emailService.sendVerificationEmail(user,siteUrl);
        Optional<Role> role = roleRepository.findByRoleName("USER");
        if (role.isEmpty()) {
            role = Optional.of(new Role());
            role.get().setRoleName("USER");
            role.get().setActive(true);
            role.get().setDescription("User role");

            Optional<Permission> permission = permissionRepository.findById(5L);
            if (permission.isPresent()) {
                role.get().setPermissions(Collections.singletonList(permission.get()));
            } else {
                role.get().setPermissions(Collections.emptyList());
            }
            role = Optional.of(roleRepository.save(role.get()));

        }
        user.setRole(role.get());

        return userMapper.toUserDTO(userRepository.save(user));
    }

    public UserDTO verifyUser(String token) throws ResourceNotFoundException {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));

        user.setActive(true);
        user.setVerificationToken(null);
        User updatedUser = userRepository.save(user);

        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        // Store refresh token in http only cookie
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        // refreshTokenCookie.setSecure(true); // HTTPS
        refreshTokenCookie.setPath("/");
        // prevent CSRF attacks
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(refreshTokenCookie);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        Jwt jwtRefreshToken = jwtDecoder.decode(refreshToken);
        String username = jwtUtils.getUsername(jwtRefreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponse.builder()
                .accessToken(jwtUtils.generateAccessToken(user))
                .build();
    }

    @Override
    public void logout(HttpServletResponse response) throws ResourceNotFoundException {
        String email = auditAware.getCurrentAuditor().orElse("");

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Access Token not valid");
        }
        SecurityContextHolder.clearContext();
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
    }

    @Override
    public UserDTO processOAuthPostLogin(OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();

        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setFirstName(name.split(" ")[0]);
            user.setLastName(name.split(" ").length > 1 ? name.split(" ")[1] : "");
            user.setPassword("");

            Optional<Role> role = roleRepository.findByRoleName("USER");
            if (role.isEmpty()) {
                role = Optional.of(new Role());
                role.get().setRoleName("USER");
                role.get().setActive(true);
                role.get().setDescription("User role");
                role = Optional.of(roleRepository.save(role.get()));
            }

            user.setRole(role.get());
            user = userRepository.save(user);
        }

        return userMapper.toUserDTO(user);
    }

    @Override
    public void forgotPassword(String email, String siteUrl) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(2)); // 2 tiếng hết hạn
        userRepository.save(user);

        String resetPasswordLink = siteUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user, resetPasswordLink);
    }

    @Override
    public void resetPassword(String token, String newPassword) throws ResourceNotFoundException {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);
    }

    @Override
    public void verifyResetToken(String token) throws ResourceNotFoundException {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("Token has expired");
        }
    }
}