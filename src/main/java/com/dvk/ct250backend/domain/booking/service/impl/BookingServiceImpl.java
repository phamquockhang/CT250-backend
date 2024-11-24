package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.BookingMapper;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.repository.CouponRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.booking.service.BookingPassengerService;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import com.dvk.ct250backend.domain.booking.utils.BookingCodeUtils;
import com.dvk.ct250backend.domain.common.service.EmailService;
import com.dvk.ct250backend.domain.common.service.LockService;
import com.dvk.ct250backend.domain.common.service.RedisService;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    BookingPassengerService bookingPassengerService;
    LockService lockService;
    BookingFlightService bookingFlightService;
    BookingCodeUtils bookingCodeUtils;
    EmailService emailService;
    RedisService redisService;
    CouponRepository couponRepository;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;

    @Override
    @Transactional
    public BookingDTO createInitBooking(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingStatus(BookingStatusEnum.INIT);
        if (booking.getCoupon() != null && booking.getCoupon().getCouponId() == null) {
            couponRepository.save(booking.getCoupon());
        }
        Map<String, Passenger> passengerMap = new HashMap<>();
        AtomicBoolean isPrimaryContactSet = new AtomicBoolean(false);
        String randomAlphanumeric = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        String passengerGroup = randomAlphanumeric + "-" +
                booking.getBookingFlights().stream()
                        .map(bookingFlight -> bookingFlight.getFlight().getFlightId().toString())
                        .collect(Collectors.joining("-"));

        booking.getBookingFlights().forEach(bookingFlight -> {
            bookingFlight.setBooking(booking);
            bookingPassengerService.processBookingPassengers(bookingFlight, passengerMap, isPrimaryContactSet, passengerGroup);
        });
        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toBookingDTO(savedBooking);
    }

    @Override
    @Transactional
    public BookingDTO reserveBooking(Integer bookingId, BookingDTO bookingDTO) {
        String lockKey = "booking_" + bookingId;
        boolean lockAcquired = lockService.acquireLock(lockKey, 30, TimeUnit.SECONDS);
        if (!lockAcquired) {
            throw new RuntimeException("Could not acquire lock for booking " + bookingId);
        }
        try {
            String bookingCode = bookingCodeUtils.generateBookingCode();
            Booking booking = bookingRepository.findById(bookingId).orElseThrow();
            booking.setBookingStatus(BookingStatusEnum.RESERVED);
            booking.setBookingCode(bookingCode);
            booking.getBookingFlights().forEach(bookingFlightService::processBookingFlight);

            Booking savedBooking = bookingRepository.save(booking);

            LocalDateTime paymentDeadline = LocalDateTime.now().plusHours(1);
            int timeoutInMillis = (int) java.time.Duration.between(LocalDateTime.now(), paymentDeadline).toMillis();
            String redisKey = "booking:" + bookingId;
            redisService.set(redisKey, bookingId, timeoutInMillis);
            emailService.sendTemporaryBookingCodeEmail(bookingCode, paymentDeadline);
            BookingDTO reservedBookingDTO = bookingMapper.toBookingDTO(savedBooking);
            reservedBookingDTO.setPaymentDeadline(paymentDeadline);
            return reservedBookingDTO;
        } finally {
            lockService.releaseLock(lockKey);
        }
    }

    @Override
    public Page<BookingDTO> getBookings(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Booking> spec = getBookingSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Booking.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Booking> bookingPage = bookingRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(bookingPage.getTotalPages())
                .total(bookingPage.getTotalElements())
                .build();
        return Page.<BookingDTO>builder()
                .meta(meta)
                .content(bookingPage.getContent().stream()
                        .map(bookingMapper::toBookingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteBooking(Integer bookingId) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        bookingRepository.delete(booking);
    }

    @Override
    public BookingDTO updateBooking(Integer bookingId, BookingDTO bookingDTO) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        bookingMapper.updateBookingFromDTO(booking, bookingDTO);
        return bookingMapper.toBookingDTO(bookingRepository.save(booking));
    }

    private Specification<Booking> getBookingSpec(Map<String, String> params) {
        Specification<Booking> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) -> {
                query.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("bookingCode"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(
                                        criteriaBuilder.concat(
                                                criteriaBuilder.concat(root.get("bookingFlights").get("bookingPassengers").get("passenger").get("lastName"), " "),
                                                root.get("bookingFlights").get("bookingPassengers").get("passenger").get("firstName")
                                        )
                                )),
                                likePattern
                        )
                );
            });
        }
        if (params.containsKey("status")) {
            List<SearchCriteria> bookingStatusCriteria = requestParamUtils.getSearchCriteria(params, "bookingStatus");
            if (!bookingStatusCriteria.isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    List<Predicate> predicates = bookingStatusCriteria.stream()
                            .map(criteria -> cb.equal(root.get(criteria.getKey()), criteria.getValue()))
                            .toList();
                    return cb.or(predicates.toArray(new Predicate[0]));
                });
            }
        }
        return spec;
    }

}