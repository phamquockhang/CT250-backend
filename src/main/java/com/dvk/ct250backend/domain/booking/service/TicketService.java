package com.dvk.ct250backend.domain.booking.service;

import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.TicketDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Ticket;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TicketService {
   void createTicketsForBooking(Booking booking) throws Exception;

   void updatePdfUrl(Integer bookingId, String pdfUrl) throws ResourceNotFoundException;

   void updateTicket(Integer ticketId, TicketDTO ticketDTO) throws ResourceNotFoundException;

   void deleteTicket(Integer ticketId) throws ResourceNotFoundException;

   Page<TicketDTO> getTickets(Map<String, String> params);

   void exportPdfForPassengersAndUploadCloudinary(Integer bookingId) throws ResourceNotFoundException, IOException, WriterException;

   Integer getLast30DaysTicketCount();
}
