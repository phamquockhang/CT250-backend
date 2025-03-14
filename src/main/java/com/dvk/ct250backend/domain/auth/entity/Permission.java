package com.dvk.ct250backend.domain.auth.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_id_seq")
    @SequenceGenerator(name = "permission_id_seq", sequenceName = "permissions_seq", allocationSize = 1)
    Long permissionId;

    String name;
    String apiPath;
    String method;
    String module;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Role> roles;

}
