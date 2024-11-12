package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import com.dvk.ct250backend.domain.booking.utils.BookingCodeUtils;
import com.dvk.ct250backend.domain.common.service.EmailService;
import com.dvk.ct250backend.domain.common.service.LockService;
import com.dvk.ct250backend.domain.common.service.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    BookingPassengerService bookingPassengerService;
    LockService lockService;
    BookingFlightService bookingFlightService;
    BookingCodeUtils bookingCodeUtils;
    EmailService emailService;
    RedisService redisService;

    @Override
    @Transactional
    public BookingDTO createInitBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingStatus(BookingStatusEnum.INIT);
        Map<String, Passenger> passengerMap = new HashMap<>();
        AtomicBoolean isPrimaryContactSet = new AtomicBoolean(false);
        String passengerGroup = UUID.randomUUID().toString();

        booking.getBookingFlights().forEach(bookingFlight -> {
            bookingFlight.setBooking(booking);
            bookingPassengerService.processBookingPassengers(bookingFlight, passengerMap, isPrimaryContactSet, passengerGroup);
        });

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDTO(savedBooking);
    }

    @Override
    @Transactional
    public BookingDTO reserveBooking(Integer bookingId, BookingDTO bookingDTO) {
        String lockKey = "booking_" + bookingId;
        boolean lockAcquired = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
        if (!lockAcquired) {
            throw new RuntimeException("Could not acquire lock for booking " + bookingId);
        }
        try {
            String bookingCode = bookingCodeUtils.generateBookingCode();
            Booking booking = bookingRepository.findById(bookingId).orElseThrow();
            booking.setBookingStatus(BookingStatusEnum.RESERVED);
            booking.setBookingCode(bookingCode);
            booking.getBookingFlights().forEach(bookingFlightService::processBookingFlight);

            Booking savedBooking = bookingRepository.save(booking);

            LocalDateTime paymentDeadline = LocalDateTime.now().plusHours(1);
            int timeout = (int) java.time.Duration.between(LocalDateTime.now(), paymentDeadline).getSeconds();
            String redisKey = "booking:" + bookingId;
            redisService.set(redisKey, bookingId, timeout);
            emailService.sendTemporaryBookingCodeEmail(bookingCode, paymentDeadline);
            BookingDTO reservedBookingDTO = bookingMapper.toBookingDTO(savedBooking);
            reservedBookingDTO.setPaymentDeadline(paymentDeadline);
            return reservedBookingDTO;
        } finally {
            lockService.releaseLock(lockKey);
        }
    }

}