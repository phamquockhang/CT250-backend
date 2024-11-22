package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.TicketDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Ticket;
import com.dvk.ct250backend.domain.booking.enums.TicketStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.TicketMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.repository.TicketRepository;
import com.dvk.ct250backend.domain.booking.service.TicketService;
import com.dvk.ct250backend.domain.booking.utils.TicketNumberUtils;
import com.dvk.ct250backend.domain.common.service.EmailService;
import com.dvk.ct250backend.infrastructure.utils.DateUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketServiceImpl implements TicketService {

    TicketRepository ticketRepository;
    TicketNumberUtils ticketNumberUtils;
    EmailService emailService;
    BookingRepository bookingRepository;
    TicketMapper ticketMapper;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    DateUtils dateUtils;

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

    @Override
    @Transactional
    public void updatePdfUrl(Integer bookingId, String pdfUrl) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        booking.getBookingFlights().forEach(bookingFlight -> {
            bookingFlight.getBookingPassengers().forEach(bookingPassenger -> {
                bookingPassenger.getTickets().forEach(ticket -> {
                    ticket.setPdfUrl(pdfUrl);
                    ticketRepository.save(ticket);
                });
            });
        });
    }

    @Override
    @Transactional
    public void updateTicket(Integer ticketId, TicketDTO ticketDTO) throws ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        ticketMapper.updateTicketFromDTO(ticket, ticketDTO);
        ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Integer ticketId) throws ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        ticketRepository.delete(ticket);
    }

    @Override
    public Page<TicketDTO> getTickets(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Ticket> spec = getTicketSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Ticket.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Ticket> ticketPage = ticketRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(ticketPage.getTotalPages())
                .total(ticketPage.getTotalElements())
                .build();
        return Page.<TicketDTO>builder()
                .meta(meta)
                .content(ticketPage.getContent().stream()
                        .map(ticketMapper::toTicketDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Ticket> getTicketSpec(Map<String, String> params) {
        Specification<Ticket> spec = Specification.where(null);

        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) -> {
                query.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.equal(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("ticketNumber"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("bookingPassenger").get("passengerGroup"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(
                                        criteriaBuilder.concat(
                                                criteriaBuilder.concat(root.get("bookingPassenger").get("passenger").get("lastName"), " "),
                                                root.get("bookingPassenger").get("passenger").get("firstName")
                                        )
                                )),
                                likePattern
                        ),
                        criteriaBuilder.equal(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("bookingPassenger").get("bookingFlight").get("bookingFlight").get("booking").get("bookingCode"))),
                                likePattern
                        )
                );
            });
        }

        if (params.containsKey("startDate") && params.containsKey("type")) {
            String startDateStr = params.get("startDate");
            String type = params.get("type");
            LocalDate startDate = dateUtils.parseDate(startDateStr, type);
            spec = spec.and((root, query, cb) -> {
                LocalDate endDate;
                if (type.equalsIgnoreCase("date")) {
                    endDate = startDate.plusDays(1);
                } else if (type.equalsIgnoreCase("month")) {
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()).plusDays(1);
                } else if (type.equalsIgnoreCase("quarter")) {
                    endDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1).plusDays(1);
                } else if (type.equalsIgnoreCase("year")) {
                    endDate = startDate.withDayOfYear(startDate.lengthOfYear()).plusDays(1);
                } else {
                    throw new IllegalArgumentException("Invalid date type: " + type);
                }
                return cb.between(root.get("createdAt"), startDate.atStartOfDay(), endDate.atStartOfDay());
            });
        }

        if(params.containsKey("startDate") && params.containsKey("endDate")) {
            String startDateStr = params.get("startDate");
            LocalDate startDate = dateUtils.parseDate(startDateStr, "date");
            String endDateStr = params.get("endDate");
            LocalDate endDate = dateUtils.parseDate(endDateStr, "date").plusDays(1);
            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startDate.atStartOfDay(), endDate.atStartOfDay()));
        }

        return spec;
    }

}