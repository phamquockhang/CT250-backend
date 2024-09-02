package com.dvk.ct250backend.infrastructure.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final ApplicationContext applicationContext;

    @Value("${application.security.jwt.access-token-validity-in-seconds}")
    private long accessTokenDurationInSeconds;

    @Value("${application.security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenDurationInSeconds;

    public String generateAccessToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, new HashMap<>(), accessTokenDurationInSeconds);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, new HashMap<>(), refreshTokenDurationInSeconds);
    }

    private String generateJwtToken(UserDetails userDetails,
                                    Map<String, Object> extraClaims,
                                    long tokenDurationInSeconds){
        JwtEncoder jwtEncoder = applicationContext.getBean(JwtEncoder.class);
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(tokenDurationInSeconds, ChronoUnit.SECONDS))
                .build();
        if(!extraClaims.isEmpty()){
            claimsSet.getClaims().putAll(extraClaims);
        }
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
        final String userEmail = getUsername(jwtToken);
        return !isTokenExpired(jwtToken) && userEmail.equals(userDetails.getUsername());
    }

}
