package com.dvk.ct250backend.domain.auth.repository;

import com.dvk.ct250backend.domain.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> , JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleName(String name);
    boolean existsRoleByRoleId(Long id);
}
