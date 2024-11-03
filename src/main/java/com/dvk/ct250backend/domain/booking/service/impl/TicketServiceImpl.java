package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Ticket;
import com.dvk.ct250backend.domain.booking.enums.TicketStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.TicketRepository;
import com.dvk.ct250backend.domain.booking.service.TicketService;
import com.dvk.ct250backend.domain.booking.utils.TicketNumberUtils;
import com.dvk.ct250backend.domain.common.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketServiceImpl implements TicketService {

    TicketRepository ticketRepository;
    TicketNumberUtils ticketNumberUtils;
    EmailService emailService;


    @Override
    @Transactional
    public void createTicketsForBooking(Booking booking) throws Exception {
        booking.getBookingFlights().forEach(bookingFlight -> {
            bookingFlight.getBookingPassengers().forEach(bookingPassenger -> {
                Ticket ticket = Ticket.builder()
                        .bookingPassenger(bookingPassenger)
                        .status(TicketStatusEnum.BOOKED)
                        .ticketNumber(ticketNumberUtils.generateTicketNumber())
                        .build();
                ticketRepository.save(ticket);
                bookingPassenger.getTickets().add(ticket);
            });
        });
        emailService.sendTicketConfirmationEmail(booking);
    }


}



