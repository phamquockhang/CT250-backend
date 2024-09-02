package com.dvk.ct250backend.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedDate
    @Column(updatable = false)
    Timestamp createdAt;

    @CreatedBy
    @Column(updatable = false)
    String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    Timestamp modifiedAt;

    @LastModifiedDate
    @Column(insertable = false)
    String modifiedBy;

}