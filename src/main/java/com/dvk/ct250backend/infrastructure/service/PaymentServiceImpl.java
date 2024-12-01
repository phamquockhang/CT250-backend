//package com.dvk.ct250backend.infrastructure.service;
//
//import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
//import com.dvk.ct250backend.domain.booking.entity.Booking;
//import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
//import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
//import com.dvk.ct250backend.domain.transaction.entity.Transaction;
//import com.dvk.ct250backend.domain.transaction.service.PaymentService;
//import com.dvk.ct250backend.infrastructure.config.payment.vnpay.VNPayConfig;
//import com.dvk.ct250backend.infrastructure.utils.VNPayUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class PaymentServiceImpl implements PaymentService {
//    BookingRepository bookingRepository;
//    private final VNPayConfig vnPayConfig;
//
//    @Override
//    public VNPayResponse createVnPayPayment(HttpServletRequest request, Transaction transaction, String txnRef) throws ResourceNotFoundException {
//        Booking booking = bookingRepository.findById(transaction.getBooking().getBookingId())
//                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
//        long amount = booking.getTotalPrice().multiply(BigDecimal.valueOf(100L)).longValue();
//        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
//        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
//        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(request));
//        vnpParamsMap.put("vnp_TxnRef", txnRef);
//        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
//        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
//        String vnpSecureHash = VNPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
//        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
//        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
//        return VNPayResponse.builder()
//                .paymentUrl(paymentUrl)
//                .build();
//
//    }
//}
//


package com.dvk.ct250backend.infrastructure.service;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.entity.Booking;
import com.dvk.ct250backend.domain.booking.repository.BookingRepository;
import com.dvk.ct250backend.domain.transaction.dto.response.VNPayResponse;
import com.dvk.ct250backend.domain.transaction.entity.Transaction;
import com.dvk.ct250backend.domain.transaction.service.PaymentService;
import com.dvk.ct250backend.infrastructure.config.payment.vnpay.VNPayConfig;
import com.dvk.ct250backend.infrastructure.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    BookingRepository bookingRepository;
    private final VNPayConfig vnPayConfig;

    @Override
    public VNPayResponse createVnPayPayment(HttpServletRequest request, Transaction transaction, String txnRef) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(transaction.getBooking().getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        long amount = booking.getTotalPrice().multiply(BigDecimal.valueOf(100L)).longValue();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(booking.getBookingId());
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(request));
        vnpParamsMap.put("vnp_TxnRef", txnRef);
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return VNPayResponse.builder()
                .paymentUrl(paymentUrl)
                .build();
    }
}