package com.dvk.ct250backend.domain.auth.repository;

import com.dvk.ct250backend.domain.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);
    boolean existsByRoleName(String name);
}
