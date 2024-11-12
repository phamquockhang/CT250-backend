package com.dvk.ct250backend.domain.transaction.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.BookingFlightService;
import com.dvk.ct250backend.domain.booking.service.impl.TicketServiceImpl;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
import com.dvk.ct250backend.domain.transaction.entity.Transaction;
import com.dvk.ct250backend.domain.transaction.enums.TransactionStatusEnum;
import com.dvk.ct250backend.domain.transaction.mapper.TransactionMapper;
import com.dvk.ct250backend.domain.transaction.repository.TransactionRepository;
import com.dvk.ct250backend.domain.transaction.service.TransactionService;
import com.dvk.ct250backend.infrastructure.service.PaymentServiceImpl;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            booking.getBookingFlights().forEach(bookingFlightService::processBookingFlight);
            ticketServiceImpl.createTicketsForBooking(booking);
        } else {
            booking.setBookingStatus(BookingStatusEnum.INIT);
        }
        bookingRepository.save(booking);

        return transactionMapper.toTransactionDTO(transaction);
    }


    @Override
    public TransactionDTO updateTransaction(Integer transactionId, TransactionDTO transactionDTO) throws ResourceNotFoundException {
       Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
       transactionMapper.updateTransactionFromDTO(transaction, transactionDTO);
         return transactionMapper.toTransactionDTO(transactionRepository.save(transaction));
    }

    @Override
    public void deleteTransaction(Integer transactionId) throws ResourceNotFoundException {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

    @Override
    public Page<TransactionDTO> getAllTransactions(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(transactionPage.getTotalPages())
                .total(transactionPage.getTotalElements())
                .build();
        return Page.<TransactionDTO>builder()
                .meta(meta)
                .content(transactionPage.getContent().stream()
                        .map(transactionMapper::toTransactionDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
