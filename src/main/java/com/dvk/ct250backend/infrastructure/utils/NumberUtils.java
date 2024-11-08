package com.dvk.ct250backend.infrastructure.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class NumberUtils {

    public BigDecimal roundToThousand(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        BigDecimal thousand = new BigDecimal("1000");
        return value.divide(thousand, 0, RoundingMode.HALF_UP).multiply(thousand);
    }
}
