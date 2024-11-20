package com.dvk.ct250backend.domain.transaction.service.impl;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.booking.service.CouponService;
import com.dvk.ct250backend.domain.booking.service.impl.TicketServiceImpl;
import com.dvk.ct250backend.domain.booking.utils.BookingCodeUtils;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
import com.dvk.ct250backend.domain.transaction.entity.Transaction;
import com.dvk.ct250backend.domain.transaction.enums.TransactionStatusEnum;
import com.dvk.ct250backend.domain.transaction.mapper.TransactionMapper;
import com.dvk.ct250backend.domain.transaction.repository.TransactionRepository;
import com.dvk.ct250backend.domain.transaction.service.TransactionService;
import com.dvk.ct250backend.infrastructure.service.PaymentServiceImpl;
import com.dvk.ct250backend.infrastructure.utils.DateUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
import com.dvk.ct250backend.infrastructure.utils.VNPayUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
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
public class TransactionServiceImpl implements TransactionService {
    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;
    PaymentServiceImpl paymentService;
    BookingRepository bookingRepository;
    TicketServiceImpl ticketServiceImpl;
    BookingFlightService bookingFlightService;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    BookingCodeUtils bookingCodeUtils;
    DateUtils dateUtils;
    CouponService couponService;
    @Override
    public TransactionDTO getTransactionById(Integer transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transactionMapper::toTransactionDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(HttpServletRequest request, TransactionDTO transactionDTO) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(transactionDTO.getBooking().getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setBookingCode(bookingCodeUtils.generateBookingCode());
        booking.setBookingStatus(BookingStatusEnum.PENDING);
        bookingRepository.save(booking);

        Transaction transaction = transactionMapper.toTransaction(transactionDTO);
        transaction.setStatus(TransactionStatusEnum.PENDING);
        transaction.setBooking(booking);
        transaction.setAmount(booking.getTotalPrice());

        String txnRef = VNPayUtils.getRandomNumber(8);
        transaction.setTxnRef(txnRef);

        Transaction savedTransaction = transactionRepository.save(transaction);

        String paymentUrl = getPaymentUrlIfNeeded(request, savedTransaction);
        TransactionDTO savedTransactionDTO = transactionMapper.toTransactionDTO(savedTransaction);

        if (savedTransactionDTO.getPaymentMethod() != null) {
            savedTransactionDTO.getPaymentMethod().setPaymentUrl(paymentUrl);
        }
        return savedTransactionDTO;
    }

    private String getPaymentUrlIfNeeded(HttpServletRequest request, Transaction transaction) throws ResourceNotFoundException {
        if (transaction.getPaymentMethod() != null
                && "VNPay".equals(transaction.getPaymentMethod().getPaymentMethodName())) {
            VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request, transaction, transaction.getTxnRef());
            return vnPayResponse.getPaymentUrl();
        }
        return "";
    }

    @Override
    @Transactional
    public TransactionDTO handleVNPayCallback(VNPayCallbackRequest request) throws Exception {
        String status = request.getVnp_ResponseCode();

        Transaction transaction = transactionRepository.findByTxnRef(request.getVnp_TxnRef())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        transaction.setStatus("00".equals(status) ? TransactionStatusEnum.COMPLETED : TransactionStatusEnum.FAILED);
        transactionRepository.save(transaction);

        String bookingCode = request.getVnp_OrderInfo();
        Booking booking = bookingRepository.findById(transaction.getBooking().getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if ("00".equals(status)) {
            booking.setBookingCode(bookingCode);
            booking.setBookingStatus(BookingStatusEnum.PAID);
            if (booking.getCoupon() != null) {
                couponService.decreaseCouponMaxUsage(booking.getCoupon());
            }
            booking.getBookingFlights().forEach(bookingFlightService::processBookingFlight);
            ticketServiceImpl.createTicketsForBooking(booking);
        } else {
            booking.setBookingStatus(BookingStatusEnum.INIT);
        }
        bookingRepository.save(booking);

        return transactionMapper.toTransactionDTO(transaction);
    }


    @Override
    public Page<TransactionDTO> getAllTransactions(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Transaction> spec = getTransactionSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Transaction.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(transactionPage.getTotalPages())
                .total(transactionPage.getTotalElements())
                .build();
        return Page.<TransactionDTO>builder()
                .meta(meta)
                .content(transactionPage.getContent().stream()
                        .map(transaction -> {
                            TransactionDTO transactionDTO = transactionMapper.toTransactionDTO(transaction);
                            setPassengerName(transactionDTO, transaction.getBooking());
                            transactionDTO.setBookingCode(transaction.getBooking().getBookingCode());
                            return transactionDTO;
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Transaction> getTransactionSpec(Map<String, String> params) {
        Specification<Transaction> spec = Specification.where(null);

        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) -> {
                query.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("booking").get("bookingCode"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("txnRef"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(
                                        criteriaBuilder.concat(
                                                criteriaBuilder.concat(root.get("booking").get("bookingFlights").get("bookingPassengers").get("passenger").get("lastName"), " "),
                                                root.get("booking").get("bookingFlights").get("bookingPassengers").get("passenger").get("firstName")
                                        )
                                )),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("paymentMethod").get("paymentMethodName"))),
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

    private void setPassengerName(TransactionDTO transactionDTO, Booking booking) {
        booking.getBookingFlights().stream()
                .flatMap(bookingFlight -> bookingFlight.getBookingPassengers().stream())
                .filter(BookingPassenger::getIsPrimaryContact)
                .findFirst()
                .ifPresent(primaryPassenger -> transactionDTO.setPassengerName((primaryPassenger.getPassenger().getLastName() + " " + primaryPassenger.getPassenger().getFirstName()).trim()));
    }
}