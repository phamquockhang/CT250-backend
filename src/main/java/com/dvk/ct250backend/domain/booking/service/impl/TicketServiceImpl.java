package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.TicketDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Ticket;
import com.dvk.ct250backend.domain.booking.enums.TicketStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.TicketMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.repository.TicketRepository;
import com.dvk.ct250backend.domain.booking.service.TicketService;
import com.dvk.ct250backend.domain.booking.utils.TicketNumberUtils;
import com.dvk.ct250backend.domain.common.service.EmailService;
import com.dvk.ct250backend.infrastructure.utils.*;
import com.google.zxing.WriterException;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
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
    TemplateEngine templateEngine;
    FileUtils fileUtils;

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
                        .map(ticket -> {
                            TicketDTO ticketDTO = ticketMapper.toTicketDTO(ticket);
                            ticketDTO.setPassengerName(ticket.getBookingPassenger().getPassenger().getLastName() + " " + ticket.getBookingPassenger().getPassenger().getFirstName());
                            ticketDTO.setBookingCode(ticket.getBookingPassenger().getBookingFlight().getBooking().getBookingCode());
                            ticketDTO.setPassengerGroup(ticket.getBookingPassenger().getPassengerGroup());
                            ticketDTO.setPhoneNumber(ticket.getBookingPassenger().getPassenger().getPhoneNumber());
                            return ticketDTO;
                        })
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
                        criteriaBuilder.like(
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
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("bookingPassenger").get("passenger").get("phoneNumber"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("bookingPassenger").get("bookingFlight").get("booking").get("bookingCode"))),
                                likePattern
                        )
                );
            });
        }

        if (params.containsKey("status")) {
            List<SearchCriteria> transactionStatusCriteria = requestParamUtils.getSearchCriteria(params, "status");
            if (!transactionStatusCriteria.isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    List<Predicate> predicates = transactionStatusCriteria.stream()
                            .map(criteria -> cb.equal(root.get(criteria.getKey()), criteria.getValue()))
                            .toList();
                    return cb.or(predicates.toArray(new Predicate[0]));
                });
            }
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

    @Override
    @Transactional
    public void exportPdfForPassengersAndUploadCloudinary(Integer bookingId) throws ResourceNotFoundException, IOException, WriterException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        for (BookingFlight bookingFlight : booking.getBookingFlights()) {
            for (BookingPassenger bookingPassenger : bookingFlight.getBookingPassengers()) {
                StringBuilder qrCodeText = new StringBuilder();
                qrCodeText.append("Booking Code: ").append(booking.getBookingCode()).append("\n");
                qrCodeText.append("Flight ID: ").append(bookingFlight.getFlight().getFlightId()).append("\n");
                qrCodeText.append("Departure: ").append(bookingFlight.getFlight().getRoute().getDepartureAirport().getAirportCode())
                        .append(" - ").append(bookingFlight.getFlight().getRoute().getDepartureAirport().getAirportName()).append("\n");
                qrCodeText.append("Arrival: ").append(bookingFlight.getFlight().getRoute().getArrivalAirport().getAirportCode())
                        .append(" - ").append(bookingFlight.getFlight().getRoute().getArrivalAirport().getAirportName()).append("\n");
                qrCodeText.append("Departure Time: ").append(bookingFlight.getFlight().getDepartureDateTime()).append("\n");
                qrCodeText.append("Arrival Time: ").append(bookingFlight.getFlight().getArrivalDateTime()).append("\n");
                qrCodeText.append("Passenger: ").append(bookingPassenger.getPassenger().getFirstName())
                        .append(" ").append(bookingPassenger.getPassenger().getLastName()).append("\n");
                qrCodeText.append("Seat: ").append(bookingPassenger.getSeat().getSeatCode()).append("\n");
                qrCodeText.append("Ticket Number: ").append(bookingPassenger.getTickets().getFirst().getTicketNumber()).append("\n");

                String qrCodeImage = QrCodeGeneratorUtils.generateQRCodeImage(qrCodeText.toString(), 200, 200);
                Context context = new Context();
                context.setVariable("qrCodeImage", qrCodeImage);
                context.setVariable("booking", booking);
                context.setVariable("bookingPassenger", bookingPassenger);

                String pdfContent = templateEngine.process("ticket-passenger", context);
                byte[] pdfData = PdfGeneratorUtils.generateTicketPdf(pdfContent);
                File tempFile = fileUtils.saveTempFile(pdfData, "temp_ticket.pdf");
                String cloudinaryUrl = fileUtils.uploadFileToCloudinary(tempFile);

                for (Ticket ticket : bookingPassenger.getTickets()) {
                    ticket.setPdfUrl(cloudinaryUrl);
                    ticketRepository.save(ticket);
                }
            }
        }
    }

}