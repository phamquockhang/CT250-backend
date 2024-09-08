package com.dvk.ct250backend.infrastructure.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final ApplicationContext applicationContext;

    @Value("${application.security.jwt.access-token-validity-in-seconds}")
    private long accessTokenDurationInSeconds;

    @Value("${application.security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenDurationInSeconds;

    public String generateAccessToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, accessTokenDurationInSeconds);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, refreshTokenDurationInSeconds);
    }

    private String generateJwtToken(UserDetails userDetails, long tokenDurationInSeconds) {
        JwtEncoder jwtEncoder = applicationContext.getBean(JwtEncoder.class);
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(tokenDurationInSeconds, ChronoUnit.SECONDS))
                .build();
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
    }

    public String getUsername(Jwt jwtToken) {
        return jwtToken.getSubject();
    }

    private boolean isTokenExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails) {
        return !isTokenExpired(jwtToken) && getUsername(jwtToken).equals(userDetails.getUsername());
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
}