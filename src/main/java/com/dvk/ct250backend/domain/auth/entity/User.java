package com.dvk.ct250backend.domain.auth.entity;

import com.dvk.ct250backend.domain.common.BaseEntity;
import com.dvk.ct250backend.domain.country.entity.Country;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
//public class User extends BaseEntity implements UserDetails {
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID userId;
    String email;
    String password;
    String firstName;
    String lastName;
    String id;
    boolean gender;
    String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "country_id")
    Country country;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "role_binding",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    Set<Role> roles = new HashSet<>();

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
//    }

//    @Override
//    public String getUsername() {
//        return email;
//    }
}
