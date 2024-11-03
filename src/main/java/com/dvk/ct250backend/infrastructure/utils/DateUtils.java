package com.dvk.ct250backend.infrastructure.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateUtils {

    public boolean isInDateRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return ((date.isAfter(startDate) || date.equals(startDate)) &&
                (date.isBefore(endDate) || date.isEqual(endDate)));
    }
}
