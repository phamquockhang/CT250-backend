package com.dvk.ct250backend.infrastructure.config.permission;

import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.infrastructure.audit.AuditAwareImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Optional;


@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomPermissionInterceptor implements HandlerInterceptor {
    private final AuditAwareImpl auditAware;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();
        logRequestDetails(path, httpMethod);

        String email = auditAware.getCurrentAuditor().orElse("");
        if (!email.isEmpty()) {
            Optional<User> user = userRepository.findUserByEmail(email);
            if (user.isPresent()) {
                checkPermissions(user.orElse(null), path, httpMethod);
            }
        }

        return true;
    }

    private void logRequestDetails(String path,  String httpMethod) {
        log.info("PATH: {}, METHOD: {}", path, httpMethod);
    }

    private void checkPermissions(User user, String path, String httpMethod) throws AccessDeniedException {
        Role role = user.getRole();
        if (role != null) {
            List<Permission> permissions = role.getPermissions();
            boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                    && item.getMethod().equals(httpMethod));

            if (!isAllow) {
                throw new AccessDeniedException("You do not have permission to access endpoint: " + path);
            }
        } else {
            throw new AccessDeniedException("You do not have permission to access endpoint: " + path);
        }
    }
}