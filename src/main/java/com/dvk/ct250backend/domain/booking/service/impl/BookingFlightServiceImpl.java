////package com.dvk.ct250backend.domain.booking.service.impl;
////
////import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
////import com.dvk.ct250backend.domain.booking.repository.BookingFlightRepository;
////import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
////import com.dvk.ct250backend.domain.flight.entity.Flight;
////import com.dvk.ct250backend.domain.flight.entity.TicketClass;
////import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
////import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
////import com.dvk.ct250backend.domain.flight.repository.TicketClassRepository;
////import com.dvk.ct250backend.infrastructure.service.LockService;
////import lombok.AccessLevel;
////import lombok.RequiredArgsConstructor;
////import lombok.experimental.FieldDefaults;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.util.Collections;
////import java.util.Comparator;
////import java.util.concurrent.TimeUnit;
////
////@Service
////@RequiredArgsConstructor
////@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
////public class BookingFlightServiceImpl implements BookingFlightService {
////
////    FlightRepository flightRepository;
////    BookingFlightRepository bookingFlightRepository;
////    TicketClassRepository ticketClassRepository;
////    LockService lockService;
////
////    @Override
////    @Transactional
////    public void saveBookingFlight(BookingFlight bookingFlight, boolean reserveSeat) {
////        String lockKey = "flight_" + bookingFlight.getFlight().getFlightId();
////        boolean lockAcquired = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
////
////        if (!lockAcquired) {
////            throw new RuntimeException("Could not acquire lock for flight booking");
////        }
////
////        try {
////            Flight flight = flightRepository.findById(bookingFlight.getFlight().getFlightId()).orElseThrow();
////            TicketClass ticketClass = ticketClassRepository.findById(bookingFlight.getTicketClass().getTicketClassId()).orElseThrow();
////            bookingFlight.setFlight(flight);
////            bookingFlight.setTicketClass(ticketClass);
////            bookingFlight = bookingFlightRepository.save(bookingFlight);
////
////            flight.getSeatAvailability().sort(Comparator.comparing(seatAvailability -> seatAvailability.getSeat().getSeatCode()));
////            Collections.shuffle(flight.getSeatAvailability());
////            int passengers = bookingFlight.getBooking().getBookingPassengers().size();
////            for (int i = 0; i < passengers; i++) {
////                flight.getSeatAvailability().stream()
////                        .filter(seatAvailability -> seatAvailability.getSeat().getTicketClass().equals(ticketClass.getTicketClassName()) &&
////                                seatAvailability.getStatus() == SeatAvailabilityStatus.AVAILABLE)
////                        .findFirst()
////                        .ifPresent(seatAvailability -> seatAvailability.setStatus(SeatAvailabilityStatus.BOOKED));
////            }
////            flightRepository.save(flight);
////
////        } finally {
////            lockService.releaseLock(lockKey);
////        }
////    }
////}
////
////
//
//package com.dvk.ct250backend.domain.booking.service.impl;
//
//import com.dvk.ct250backend.domain.booking.entity.BookingFlight;
//import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
//import com.dvk.ct250backend.domain.booking.entity.Passenger;
//import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
//import com.dvk.ct250backend.domain.booking.repository.BookingFlightRepository;
//import com.dvk.ct250backend.domain.booking.repository.PassengerRepository;
//import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
//import com.dvk.ct250backend.domain.flight.entity.Flight;
//import com.dvk.ct250backend.domain.flight.entity.TicketClass;
//import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
//import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
//import com.dvk.ct250backend.domain.flight.repository.TicketClassRepository;
//import com.dvk.ct250backend.infrastructure.service.LockService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class BookingFlightServiceImpl implements BookingFlightService {
//
//    FlightRepository flightRepository;
//    BookingFlightRepository bookingFlightRepository;
//    TicketClassRepository ticketClassRepository;
//    PassengerRepository passengerRepository;
//    LockService lockService;
//
//    @Override
//    @Transactional
//    public void saveBookingFlight(BookingFlight bookingFlight, boolean reserveSeat) {
//        String lockKey = "flight_" + bookingFlight.getFlight().getFlightId();
//        boolean lockAcquired = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
//
//        if (!lockAcquired) {
//            throw new RuntimeException("Could not acquire lock for flight booking");
//        }
//
//        try {
//            Flight flight = flightRepository.findById(bookingFlight.getFlight().getFlightId()).orElseThrow();
//            TicketClass ticketClass = ticketClassRepository.findById(bookingFlight.getTicketClass().getTicketClassId()).orElseThrow();
//            bookingFlight.setFlight(flight);
//            bookingFlight.setTicketClass(ticketClass);
//            bookingFlight = bookingFlightRepository.save(bookingFlight);
//
//            // Save all passengers first
//            List<BookingPassenger> bookingPassengers = bookingFlight.getBooking().getBookingPassengers();
//            bookingPassengers.forEach(bookingPassenger -> {
//                Passenger passenger = bookingPassenger.getPassenger();
//                if (passenger.getPassengerId() == null) {
//                    passenger = passengerRepository.save(passenger);
//                }
//                bookingPassenger.setPassenger(passenger);
//            });
//
//            flight.getSeatAvailability().sort(Comparator.comparing(seatAvailability -> seatAvailability.getSeat().getSeatCode()));
//            Collections.shuffle(flight.getSeatAvailability());
//
//            if (reserveSeat) {
//                List<Passenger> adults = bookingPassengers.stream()
//                        .map(BookingPassenger::getPassenger)
//                        .filter(passenger -> passenger.getPassengerType() == PassengerTypeEnum.ADULT)
//                        .collect(Collectors.toList());
//                List<Passenger> infants = bookingPassengers.stream()
//                        .map(BookingPassenger::getPassenger)
//                        .filter(passenger -> passenger.getPassengerType() == PassengerTypeEnum.INFANT)
//                        .collect(Collectors.toList());
//
//                for (int i = 0; i < adults.size(); i++) {
//                    Passenger adult = adults.get(i);
//                    Passenger infant = i < infants.size() ? infants.get(i) : null;
//
//                    // Find and book a seat for the adult
//                    flight.getSeatAvailability().stream()
//                            .filter(seatAvailability -> seatAvailability.getSeat().getTicketClass().equals(ticketClass.getTicketClassName()) &&
//                                    seatAvailability.getStatus() == SeatAvailabilityStatus.AVAILABLE)
//                            .findFirst()
//                            .ifPresent(seatAvailability -> seatAvailability.setStatus(SeatAvailabilityStatus.BOOKED));
//
//                    // Find and book a seat for the infant if available
//                    if (infant != null) {
//                        flight.getSeatAvailability().stream()
//                                .filter(seatAvailability -> seatAvailability.getSeat().getTicketClass().equals(ticketClass.getTicketClassName()) &&
//                                        seatAvailability.getStatus() == SeatAvailabilityStatus.AVAILABLE)
//                                .findFirst()
//                                .ifPresent(seatAvailability -> seatAvailability.setStatus(SeatAvailabilityStatus.BOOKED));
//                    }
//                }
//                flightRepository.save(flight);
//            }
//        } finally {
//            lockService.releaseLock(lockKey);
//        }
//    }
//}
