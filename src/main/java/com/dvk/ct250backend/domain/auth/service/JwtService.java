package com.dvk.ct250backend.domain.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;


public interface JwtService {
    String extractUsername(String token);
    Date extractExpiration(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
}
