package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Ticket;
import com.dvk.ct250backend.domain.booking.enums.TicketStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.TicketRepository;
import com.dvk.ct250backend.domain.booking.service.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketServiceImpl implements TicketService {
    TicketRepository ticketRepository;
    public Ticket createTicket(Booking booking, String seatCode, String flightId) {
        Ticket ticket = Ticket.builder()
                .booking(booking)
                .seatCode(seatCode)
                //.flightId(flightId)
                .status(TicketStatusEnum.BOOKED)
                .build();
        return ticketRepository.save(ticket);
    }
}
