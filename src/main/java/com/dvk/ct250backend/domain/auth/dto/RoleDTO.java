package com.dvk.ct250backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {
    Integer roleId;
    @NotBlank(message = "Role name is required")
    String roleName;
    @NotBlank(message = "Description is required")
    String description;
    boolean active;

    List<PermissionDTO> permissions;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
