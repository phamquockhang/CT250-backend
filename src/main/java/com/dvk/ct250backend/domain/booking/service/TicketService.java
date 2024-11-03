package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface TicketService {
   void createTicketsForBooking(Booking booking) throws Exception;
}
