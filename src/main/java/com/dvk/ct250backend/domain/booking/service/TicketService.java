package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.domain.booking.entity.Booking;

public interface TicketService {
   void createTicketsForBooking(Booking booking) throws Exception;
}
