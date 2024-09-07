package com.dvk.ct250backend.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO {
    Timestamp createdAt;
    String createdBy;
    Timestamp modifiedAt;
    String modifiedBy;
    Timestamp updatedAt;
    String updatedBy;
}
