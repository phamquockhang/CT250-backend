package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.domain.booking.dto.TicketDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Ticket;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface TicketService {
   void createTicketsForBooking(Booking booking) throws MessagingException, IOException, DocumentException;
}
