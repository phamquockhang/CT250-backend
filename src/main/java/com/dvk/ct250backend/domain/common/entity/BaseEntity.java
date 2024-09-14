package com.dvk.ct250backend.domain.common.entity;

import com.dvk.ct250backend.infrastructure.audit.AuditAwareImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    Timestamp createdAt;

    @CreatedBy
    @Column(updatable = false)
    String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    Timestamp updatedAt;

    @Column(insertable = false)
    String updatedBy;


//    @PrePersist
//    public void handleBeforeCreate() {
//        this.createdBy = new AuditAwareImpl().getCurrentAuditor().orElse("");
//        this.createdAt = Timestamp.from(Instant.now());
//    }
//
//    @PreUpdate
//    public void handleBeforeUpdate() {
//        this.updatedBy = new AuditAwareImpl().getCurrentAuditor().orElse("");
//        this.updatedAt = Timestamp.from(Instant.now());
//    }
}