package com.dvk.ct250backend.domain.booking.schedule;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CleanUpJobUtils {
    BookingRepository bookingRepository;

    @Scheduled(fixedRate = 3600000)
    public void cleanUpInitBookings() {
        LocalDateTime cutoffTime = LocalDateTime.now().minus(3, ChronoUnit.HOURS);
        List<Booking> bookingsToDelete = bookingRepository.findByBookingStatusAndCreatedAtBefore(BookingStatusEnum.INIT, cutoffTime);
        bookingsToDelete.forEach(booking -> booking.setBookingStatus(BookingStatusEnum.CANCELLED));
        bookingRepository.saveAll(bookingsToDelete);
    }
}
