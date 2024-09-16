package com.dvk.ct250backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionDTO {
    Long permissionId;
    String name;
    String apiPath;
    String method;
    String module;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
