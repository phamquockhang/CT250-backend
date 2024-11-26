package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.domain.flight.entity.Seat;
import com.dvk.ct250backend.domain.flight.entity.SeatAvailability;
import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;
import com.dvk.ct250backend.domain.flight.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SeatServiceImpl implements SeatService {

    @Override
    public Seat findAvailableSeat(List<SeatAvailability> seatAvailabilities, TicketClassEnum ticketClass) {
        return seatAvailabilities.stream()
                .filter(seatAvailability -> seatAvailability.getSeat().getTicketClass() == ticketClass &&
                        seatAvailability.getStatus() == SeatAvailabilityStatus.AVAILABLE)
                .sorted(Comparator.comparing(seatAvailability -> seatAvailability.getSeat().getSeatCode()))
                .map(SeatAvailability::getSeat)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No available seats for ticket class " + ticketClass));
    }

    @Override
    public void bookSeat(List<SeatAvailability> seatAvailabilities, Seat seat) {
        SeatAvailability seatAvailabilityToUpdate = seatAvailabilities.stream()
                .filter(seatAvailability -> seatAvailability.getSeat().equals(seat) &&
                        seatAvailability.getStatus() == SeatAvailabilityStatus.AVAILABLE)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("SeatAvailability not found for the seat"));
        seatAvailabilityToUpdate.setStatus(SeatAvailabilityStatus.BOOKED);
    }
}
