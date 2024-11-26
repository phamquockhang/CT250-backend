package com.dvk.ct250backend.infrastructure.config.redis;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisKeyExpirationListener implements MessageListener {

    private final BookingRepository bookingRepository;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        if (expiredKey.startsWith("booking:")) {
            String bookingIdStr = expiredKey.split(":")[1];
            Integer bookingId = Integer.valueOf(bookingIdStr);
            Booking booking = bookingRepository.findById(bookingId).orElse(null);
            if (booking != null && booking.getBookingStatus() == BookingStatusEnum.RESERVED) {
                booking.setBookingStatus(BookingStatusEnum.INIT);
                bookingRepository.save(booking);
                log.info("Updated booking status to INIT for booking ID: {}", bookingId);
            }
        }
    }
}