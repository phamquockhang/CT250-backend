package com.dvk.ct250backend.domain.auth.dto;

import com.dvk.ct250backend.domain.common.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

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
    Timestamp createdAt;
    Timestamp updatedAt;
}
