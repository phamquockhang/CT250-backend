//package com.dvk.ct250backend.infrastructure.audit;
//
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.lang.NonNull;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component("auditorAware")
//public class AuditAwareImpl implements AuditorAware<String> {
//    @Override
//    @NonNull
//    public Optional<String> getCurrentAuditor() {
//        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
//    }
//}
