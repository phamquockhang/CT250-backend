package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.flight.entity.Seat;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;
import com.dvk.ct250backend.domain.flight.service.SeatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingFlightServiceImpl implements BookingFlightService {

    SeatService seatService;

    @Override
    @Transactional
    public void processBookingFlight(BookingFlight bookingFlight) {
        bookingFlight.getBookingPassengers().forEach(bookingPassenger -> {
            TicketClassEnum ticketClass = bookingPassenger.getBookingFlight().getTicketClass().getTicketClassName();

            if (bookingPassenger.getIsSharedSeat()) {
                BookingPassenger adultPassenger = bookingFlight.getBookingPassengers().stream()
                        .filter(bp -> bp.getPassenger().getPassengerType() == PassengerTypeEnum.ADULT)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No adult passenger found to share seat with infant"));
                bookingPassenger.setSeat(adultPassenger.getSeat());
            } else {
                Seat availableSeat = seatService.findAvailableSeat(bookingFlight.getFlight().getSeatAvailability(), ticketClass);
                seatService.bookSeat(bookingFlight.getFlight().getSeatAvailability(), availableSeat);
                bookingPassenger.setSeat(availableSeat);
            }
        });
    }
}
