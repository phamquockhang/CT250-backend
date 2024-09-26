package com.dvk.ct250backend.domain.auth.entity;

import com.dvk.ct250backend.domain.auth.enums.GenderEnum;
import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import com.dvk.ct250backend.domain.country.entity.Country;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", columnDefinition = "UUID default gen_random_uuid()")
    UUID userId;
    String email;
    String password;
    String firstName;
    String lastName;
    String avatar;

    LocalDate dateOfBirth;

    @Column(unique = true)
    String verificationToken;

    @Column(name = "token_expiry_date")
    LocalDateTime tokenExpiryDate;

    @Column(name = "identity_number", unique = true, length = 20)
    String identityNumber;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "country_id")
    Country country;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}