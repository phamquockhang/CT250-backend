package com.dvk.ct250backend.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    Integer roleId;
    String roleName;
}
