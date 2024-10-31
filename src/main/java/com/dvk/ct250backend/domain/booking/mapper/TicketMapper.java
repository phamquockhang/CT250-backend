package com.dvk.ct250backend.domain.booking.mapper;

import com.dvk.ct250backend.domain.booking.dto.TicketDTO;
import com.dvk.ct250backend.domain.booking.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketDTO toTicketDTO(Ticket ticket);
    Ticket toTicket(TicketDTO ticketDTO);

}
