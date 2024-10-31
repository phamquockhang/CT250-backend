package com.dvk.ct250backend.domain.transaction.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.booking.service.impl.TicketServiceImpl;
import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
import com.dvk.ct250backend.domain.transaction.entity.Transaction;
import com.dvk.ct250backend.domain.transaction.enums.TransactionStatusEnum;
import com.dvk.ct250backend.domain.transaction.mapper.TransactionMapper;
import com.dvk.ct250backend.domain.transaction.repository.TransactionRepository;
import com.dvk.ct250backend.domain.transaction.service.TransactionService;
import com.dvk.ct250backend.infrastructure.service.PaymentService;
import com.dvk.ct250backend.infrastructure.utils.VNPayUtils;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionServiceImpl implements TransactionService {
    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;
    PaymentService paymentService;
    BookingRepository bookingRepository;
    TicketServiceImpl ticketServiceImpl;

    @Override
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
    public TransactionDTO handleVNPayCallback(VNPayCallbackRequest request) throws ResourceNotFoundException, MessagingException, IOException, DocumentException {
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
            ticketServiceImpl.createTicketsForBooking(booking);
        } else {
            booking.setBookingStatus(BookingStatusEnum.INIT);
        }
        bookingRepository.save(booking);

        return transactionMapper.toTransactionDTO(transaction);
    }
}
