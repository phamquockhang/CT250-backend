package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Ticket;

public interface TicketService {
    Ticket createTicket(Booking booking, String seatCode, String flightId);
}
