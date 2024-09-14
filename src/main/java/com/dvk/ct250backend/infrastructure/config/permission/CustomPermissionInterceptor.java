package com.dvk.ct250backend.infrastructure.config.permission;

import com.dvk.ct250backend.app.exception.PermissionException;
import com.dvk.ct250backend.domain.auth.entity.Permission;
import com.dvk.ct250backend.domain.auth.entity.Role;
import com.dvk.ct250backend.domain.auth.entity.User;
import com.dvk.ct250backend.domain.auth.repository.UserRepository;
import com.dvk.ct250backend.infrastructure.audit.AuditAwareImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;


public class CustomPermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private AuditAwareImpl auditAware;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        logRequestDetails(path, requestURI, httpMethod);

        String email = auditAware.getCurrentAuditor().orElse("");
        if (!email.isEmpty()) {
            User user = userRepository.findUserByEmail(email);
            if (user != null) {
                checkPermissions(user, path, httpMethod);
            }
        }

        return true;
    }

    private void logRequestDetails(String path, String requestURI, String httpMethod) {
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);
    }

    private void checkPermissions(User user, String path, String httpMethod) throws PermissionException {
        Role role = user.getRole();
        if (role != null) {
            List<Permission> permissions = role.getPermissions();
            boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                    && item.getMethod().equals(httpMethod));

            if (!isAllow) {
                throw new PermissionException("You do not have permission to access this endpoint.");
            }
        } else {
            throw new PermissionException("You do not have permission to access this endpoint.");
        }
    }
}