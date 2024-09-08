package com.dvk.ct250backend.domain.common.dto;

import com.dvk.ct250backend.infrastructure.audit.AuditAwareImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.Instant;

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
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = new AuditAwareImpl().getCurrentAuditor().orElse("");
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = new AuditAwareImpl().getCurrentAuditor().orElse("");
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
