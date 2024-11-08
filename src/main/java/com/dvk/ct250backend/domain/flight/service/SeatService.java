package com.dvk.ct250backend.domain.flight.service;

import com.dvk.ct250backend.domain.flight.entity.Seat;
import com.dvk.ct250backend.domain.flight.entity.SeatAvailability;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;

import java.util.List;

public interface SeatService {
    Seat findAvailableSeat(List<SeatAvailability> seatAvailabilities, TicketClassEnum ticketClass);
    void bookSeat(List<SeatAvailability> seatAvailabilities, Seat seat);
}
