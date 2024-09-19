package com.dvk.ct250backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {
    Long roleId;
    @NotBlank(message = "Role name is required")
    String roleName;
    String description;
    boolean active;

    List<PermissionDTO> permissions = new ArrayList<>();

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
