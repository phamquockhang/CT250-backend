package com.dvk.ct250backend.domain.booking.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketNumberUtils {
    SecureRandom random = new SecureRandom();
    public String generateTicketNumber() {
        long number = (long) (random.nextDouble() * 1_000_000_000_000_00L);
        return String.format("%013d", number);
    }
}
