package com.dvk.ct250backend.infrastructure.service.redis;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.infrastructure.config.kryo.KryoSerializer;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingExpirationListener implements MessageListener {

    private final BookingRepository bookingRepository;
    private final KryoSerializer kryoSerializer;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = (String) kryoSerializer.deserialize(message.getBody());
        Integer bookingId = Integer.parseInt(messageBody);
        log.info("Received expiration message: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            log.warn("Booking not found for booking ID: {}", bookingId);
        } else {
            log.info("Current booking status for booking ID {}: {}", bookingId, booking.getBookingStatus());
            if (booking.getBookingStatus().equals(BookingStatusEnum.RESERVED)) {
                booking.setBookingStatus(BookingStatusEnum.INIT);
                bookingRepository.save(booking);
                log.info("Updated booking status to INIT for booking ID: {}", bookingId);
            } else {
                log.warn("Booking status not RESERVED for booking ID: {}", bookingId);
            }
        }
    }


}
